package dto

import play.api.libs.json._

case class UserResponse(id: Long, email: String, name: String)

object UserResponse {
  implicit val fmt: OFormat[UserResponse] = Json.format[UserResponse]
}