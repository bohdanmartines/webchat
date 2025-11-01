package models

import play.api.libs.json.{Json, OFormat}

case class UserCreate(email: String, password: String, name: String)

object UserCreate {
  implicit val fmt: OFormat[UserCreate] = Json.format[UserCreate]
}