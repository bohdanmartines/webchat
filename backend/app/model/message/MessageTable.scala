package model.message

import slick.jdbc.MySQLProfile.api._
import slick.lifted.{ProvenShape, Tag}

import java.time.LocalDateTime

class MessageTable (tag: Tag) extends Table[Message](tag, "messages") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def chatId = column[Long]("chat_id")
  def userId = column[Long]("user_id")
  def content = column[String]("content")
  def createdAt = column[LocalDateTime]("created_at")

  override def * : ProvenShape[Message] = (id, chatId, userId, content, createdAt) <> (Message.tupled, Message.unapply)

}
