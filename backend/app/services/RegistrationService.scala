package services

import dto.{UserCreate, UserResponse}

import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class RegistrationService {

  def register(user: UserCreate): Future[Either[String, UserResponse]] = {
    Future.successful(Right(UserResponse(id = 1, username = user.username, name = "MOCK USER from Service")))
  }
}
