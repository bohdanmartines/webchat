package dto

import play.api.libs.json.{Json, OFormat}

case class Login(username: String, password: String)

object Login {
  implicit val fmt: OFormat[Login] = Json.format[Login]
}
