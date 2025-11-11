package dto

import play.api.libs.json.{Json, OFormat}

case class ChatResponse(id: Long, name: String)

object ChatResponse {
  implicit val fmt: OFormat[ChatResponse] = Json.format[ChatResponse]
}
