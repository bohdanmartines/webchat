package model

import play.api.libs.json.{Json, OFormat}

case class Chat(id: Long = 0L, name: String, creator: Long)

case class ChatWithParticipantCount(id: Long, name: String, creator: Long, participantCount: Int)

object Chat {
  implicit val fmt: OFormat[Chat] = Json.format[Chat]
}
