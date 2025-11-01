package service

import dto.{UserCreate, UserResponse}
import model.User
import repository.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RegistrationService @Inject()(repository: UserRepository)(implicit ec: ExecutionContext) {

  def register(userCreate: UserCreate): Future[Either[String, UserResponse]] = {
    repository.findByUsername(userCreate.username).flatMap {
      case Some(existing) => Future.successful(Left(s"Username [${existing.username}] already exists"))
      case None =>
        println(s"About to create user: $userCreate")
        val user = User(username = userCreate.username, passwordHash = "<PASSWORD>", name = userCreate.name) // TODO hash the password
        repository.create(user).map {
          createdUser => Right(UserResponse(createdUser.id, createdUser.username, createdUser.name))
        }
    }
  }
}
