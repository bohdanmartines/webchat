package controllers

import play.api.mvc._
import javax.inject._

import services.RegistrationService

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents,
                               reqistrationService: RegistrationService) extends BaseController {

  def register() = Action { request =>
    Ok("Registration: Implement me!")
  }

  def login() = Action {
    Ok("Login: Implement me!")
  }

  def me() = Action {
    Ok("About me: Implement me!")
  }
}
