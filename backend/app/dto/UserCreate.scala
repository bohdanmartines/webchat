package dto

import play.api.libs.json._

case class UserCreate(username: String, password: String, name: String)

object UserCreate {
  implicit val fmt: OFormat[UserCreate] = Json.format[UserCreate]
}