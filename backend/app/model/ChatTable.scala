package model

import slick.jdbc.MySQLProfile.api._
import slick.lifted.{ProvenShape, Tag}

class ChatTable(tag: Tag) extends Table[Chat](tag, "chats"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def createdBy = column[Long]("created_by")

  override def * : ProvenShape[Chat] = (id, name, createdBy) <> (Chat.tupled, Chat.unapply)
}

class ChatParticipantsTable(tag: Tag) extends Table[ChatParticipant](tag, "chat_participants") {
  def chatId = column[Long]("chat_id")
  def userId = column[Long]("user_id")

  def * = (chatId, userId) <> ((ChatParticipant.apply _).tupled, ChatParticipant.unapply)
}