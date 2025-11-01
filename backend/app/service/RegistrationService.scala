package service

import dto.{UserCreate, UserResponse}
import repository.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class RegistrationService @Inject()(repository: UserRepository){

  def register(user: UserCreate): Future[Either[String, UserResponse]] = {
    Future.successful(Right(UserResponse(id = 1, username = user.username, name = "MOCK USER from Service")))
  }
}
