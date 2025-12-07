package repository

import actor.WebSocketProtocol.NewMessage
import model.message.{Message, MessageTable}
import model.user.UserTable
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject._
import scala.concurrent.Future

@Singleton
class MessageRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit ec: scala.concurrent.ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val messages = TableQuery[MessageTable]
  private val users = TableQuery[UserTable]

  def create(messageToInsert: Message): Future[Message] = {
    val insert = (messages returning messages.map(_.id) into ((message, id) => message.copy(id = id))) += messageToInsert
    db.run(insert)
  }

  def getMessages(chatId: Long, limit: Int = 10, offset: Int = 0): Future[Seq[NewMessage]] = {
    val query = for {
      message <- messages if message.chatId === chatId
      user <- users if user.id === message.userId
    } yield (message.id, message.chatId, message.userId, user.username, message.content, message.createdAt)

    db.run(query.sortBy(_._6)
      .drop(offset)
      .take(limit)
      .result
    ).map(_.map {
      case (id, chatId, userId, username, content, createdAt) =>
        NewMessage(id, chatId, userId, username, content, createdAt)
    })
  }
}
