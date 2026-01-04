package dto

import play.api.libs.json.{Format, Json}

case class ParticipantRequest(username: String)

object ParticipantRequest {
  implicit val format: Format[ParticipantRequest] = Json.format[ParticipantRequest]
}
