package model.chat

import play.api.libs.json.{Json, OFormat}

case class ChatParticipant (id: Long = 0L, chatId: Long, userId: Long)

object ChatParticipant {
  implicit val fmt: OFormat[ChatParticipant] = Json.format[ChatParticipant]
}