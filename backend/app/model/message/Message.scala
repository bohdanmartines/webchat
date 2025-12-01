package model.message

import play.api.libs.json.{Format, Json}

import java.time.LocalDateTime

case class Message(id: Long = 0L,
                   chatId: Long,
                   userId: Long,
                   content: String,
                   createdAt: LocalDateTime = LocalDateTime.now)

object Message {
  implicit val messageFormat: Format[Message] = Json.format[Message]
}
