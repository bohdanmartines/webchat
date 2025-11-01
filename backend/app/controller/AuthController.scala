package controller

import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import dto.{Login, UserCreate, UserResponse}
import service.AuthService

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents,
                               authService: AuthService)
                              (implicit ec: ExecutionContext) extends BaseController {

  def register() = Action.async(parse.json) { request =>
    val userCreate = request.body.validate[UserCreate]
    userCreate.fold(
      errors => Future.successful(BadRequest(errors.toString)),
      user => authService.register(user).map {
        case Left(err) => Conflict(Json.obj("error" -> err))
        case Right(userResp) => Created(Json.toJson(userResp))
      }
    )
  }

  def login() = Action.async(parse.json) { request =>
    val loginRequest = request.body.validate[Login]
    loginRequest.fold(
      errors => Future.successful(BadRequest(errors.toString)),
      loginObject => authService.login(loginObject).map {
        case Left(err) => Forbidden(Json.obj("error" -> err))
        case Right(t) => Ok(Json.obj("token" -> t))
      }
    )
  }

  def me() = Action {
    Ok("About me: Implement me!")
  }
}
