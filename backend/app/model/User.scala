package model

import play.api.libs.json.{Json, OFormat}

case class User(id: Long = 0L, username: String, passwordHash: String, name: String)

object User {
  implicit val fmt: OFormat[User] = Json.format[User]
}