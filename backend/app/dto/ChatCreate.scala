package dto

import play.api.libs.json.{Json, OFormat}

case class ChatCreate(name: String)

object ChatCreate {
  implicit val fmt: OFormat[ChatCreate] = Json.format[ChatCreate]
}
