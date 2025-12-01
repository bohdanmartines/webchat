package repository

import model.message.{Message, MessageTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject._
import scala.concurrent.Future

@Singleton
class MessageRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val messages = TableQuery[MessageTable]

  def create(messageToInsert: Message): Future[Message] = {
    val insert = (messages returning messages.map(_.id) into ((message, id) => message.copy(id = id))) += messageToInsert
    db.run(insert)
  }
}
