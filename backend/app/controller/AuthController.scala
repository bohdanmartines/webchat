package controller

import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

import dto.{UserCreate, UserResponse}
import service.RegistrationService

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents,
                               reqistrationService: RegistrationService)
                              (implicit ec: ExecutionContext) extends BaseController {

  def register() = Action.async(parse.json) { request =>
    val userCreate = request.body.validate[UserCreate]
    userCreate.fold(
      errors => Future.successful(BadRequest(errors.toString)),
      user => reqistrationService.register(user).map {
        case Left(err) => Conflict(Json.obj("error" -> err))
        case Right(userResp) => Created(Json.toJson(userResp))
      }
    )
  }

  def login() = Action {
    Ok("Login: Implement me!")
  }

  def me() = Action {
    Ok("About me: Implement me!")
  }
}
