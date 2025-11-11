package model

import play.api.libs.json.{Json, OFormat}

case class Chat(id: Long = 0L, name: String, creator: Long)

object Chat {
  implicit val fmt: OFormat[Chat] = Json.format[Chat]
}
