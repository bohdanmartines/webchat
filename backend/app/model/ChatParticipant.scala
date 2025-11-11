package model

import play.api.libs.json.{Json, OFormat}

case class ChatParticipant (chatId: Long, userId: Long)

object ChatParticipant {
  implicit val fmt: OFormat[ChatParticipant] = Json.format[ChatParticipant]
}