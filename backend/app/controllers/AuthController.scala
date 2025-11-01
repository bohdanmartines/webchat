package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.RegistrationService

import javax.inject._
import scala.concurrent.Future

import dto.{UserCreate, UserResponse}

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents,
                               reqistrationService: RegistrationService) extends BaseController {

  def register() = Action.async(parse.json) { request =>
    val userCreate = request.body.validate[UserCreate]
    userCreate.fold(
      errors => Future.successful(BadRequest(s"Invalid user registration request $errors")),
      user => Future.successful(Created(Json.toJson(UserResponse(1, user.username, user.name))))
    )
  }

  def login() = Action {
    Ok("Login: Implement me!")
  }

  def me() = Action {
    Ok("About me: Implement me!")
  }
}
