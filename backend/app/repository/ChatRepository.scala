package repository

import model.{Chat, ChatParticipant, ChatParticipantsTable, ChatTable, ChatWithParticipants}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject._
import scala.concurrent.Future

@Singleton
class ChatRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                              (implicit ec: scala.concurrent.ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val chats = TableQuery[ChatTable]
  private val chatParticipants = TableQuery[ChatParticipantsTable]

  def create(chatToInsert: Chat): Future[Chat] = {
    val insertChat = (chats returning chats.map(_.id) into ((chat, id) => chat.copy(id = id))) += chatToInsert

    val action = for {
      chat <- insertChat
      _ <- chatParticipants += ChatParticipant(chatId = chat.id, userId = chat.creator)
    } yield chat

    db.run(action.transactionally)
  }

  def findById(id: Long): Future[Option[Chat]] = {
    db.run(chats.filter(_.id === id).result.headOption)
  }

  def findByUser(userId: Long): Future[Seq[ChatWithParticipants]] = {
    val query = chatParticipants.filter(_.userId === userId)
      .join(chats).on(_.chatId === _.id)
      .map(_._2)
    db.run(query.result).map(chats =>
      chats.map(chat => ChatWithParticipants(chat.id, chat.name, chat.creator, Seq.empty))
    )
  }
}
