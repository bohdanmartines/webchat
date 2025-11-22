package model

import play.api.libs.json.{Json, OFormat}

case class ChatWithParticipants(id: Long = 0L, name: String, creator: Long, participants: Seq[Long] = Seq.empty)

object ChatWithParticipants {
  implicit val fmt: OFormat[ChatWithParticipants] = Json.format[ChatWithParticipants]
}
