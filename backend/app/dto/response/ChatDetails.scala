package dto.response

import play.api.libs.json.{Json, OFormat}

case class ChatDetails(id: Long, name: String, Participants: Seq[UserResponse])

object ChatDetails {
  implicit val fmt: OFormat[ChatDetails] = Json.format[ChatDetails]
}
