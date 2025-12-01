package model

import model.user.User
import play.api.libs.json.{Json, OFormat}

case class Chat(id: Long = 0L, name: String, creator: Long)

case class ChatWithParticipantCount(id: Long, name: String, creator: Long, participantCount: Int)

case class ChatWithParticipants(id: Long = 0L, name: String, creator: Long, participants: Seq[User] = Seq.empty)

object Chat {
  implicit val fmt: OFormat[Chat] = Json.format[Chat]
}
