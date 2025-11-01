package controllers

import play.api.mvc._
import javax.inject._

class AuthController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def register() = Action {
    Ok("Registration: Implement me!")
  }

  def login() = Action {
    Ok("Login: Implement me!")
  }

  def me() = Action {
    Ok("About me: Implement me!")
  }
}
