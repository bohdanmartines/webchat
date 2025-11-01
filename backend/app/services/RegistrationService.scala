package services

import dto.{UserCreate, UserResponse}
import models.User

import javax.inject.Singleton
import scala.util.{Failure, Success, Try}

@Singleton
class RegistrationService {

  def register(user: UserCreate): User = {
    User(id = 1, username = user.username, passwordHash = "<PASSWORD>", name = "MOCK USER")
  }
}
