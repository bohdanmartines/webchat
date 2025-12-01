package repository

import model.chat.{Chat, ChatParticipant, ChatParticipantsTable, ChatTable, ChatWithParticipantCount, ChatWithParticipants}
import model.user.UserTable
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
  private val users = TableQuery[UserTable]

  def create(chatToInsert: Chat): Future[ChatWithParticipantCount] = {
    val insertChat = (chats returning chats.map(_.id) into ((chat, id) => chat.copy(id = id))) += chatToInsert

    val action = for {
      chat <- insertChat
      _ <- chatParticipants += ChatParticipant(chatId = chat.id, userId = chat.creator)
      participantCount <- chatParticipants.filter(_.chatId === chat.id).length.result
    } yield ChatWithParticipantCount(
      chat.id, chat.name, chat.creator, participantCount
    )

    db.run(action.transactionally)
  }

  def findById(id: Long): Future[Option[Chat]] = {
    db.run(chats.filter(_.id === id).result.headOption)
  }

  def findByUser(userId: Long): Future[Seq[ChatWithParticipantCount]] = {
    val query = chatParticipants.filter(_.userId === userId)  // First, find chats with the user
      .join(chats).on(_.chatId === _.id)
      .map(_._2)

    db.run(query.result).flatMap { chats =>
      val chatIds = chats.map(_.id)
      val participantsQuery = chatParticipants.filter(_.chatId inSet chatIds) // Then, find participant count for each chat
        .groupBy(_.chatId)
        .map { case (chatId, participants) => (chatId, participants.length) }

      db.run(participantsQuery.result)
        .map { counts =>
          val countMap = counts.toMap
          chats.map(chat => ChatWithParticipantCount(chat.id, chat.name, chat.creator, countMap.getOrElse(chat.id, 0)))
        }
    }
  }

  def isUserInChat(chatId: Long, userId: Long): Future[Boolean] = {
    val action = chatParticipants.filter(_.chatId === chatId).filter(_.userId === userId).exists.result
    db.run(action)
  }

  def findByIdAndUser(id: Long): Future[Option[ChatWithParticipants]] = {
    val action = for {
      chatOption <- chats.filter(_.id === id).result.headOption
      participants <- chatParticipants.filter(_.chatId === id)
        .join(users).on(_.userId === _.id)
        .map(_._2)
        .result
    } yield chatOption.map{ chat =>
      ChatWithParticipants(chat.id, chat.name, chat.creator, participants)
    }
    db.run(action)
  }
}
