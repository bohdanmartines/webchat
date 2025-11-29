package dto.response

import play.api.libs.json.{Json, OFormat}

case class ChatSummary(id: Long, name: String, participantCount: Int)

object ChatSummary {
  implicit val fmt: OFormat[ChatSummary] = Json.format[ChatSummary]
}
