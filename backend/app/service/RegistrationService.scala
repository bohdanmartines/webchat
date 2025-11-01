package service

import dto.{UserCreate, UserResponse}
import model.User
import repository.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
class RegistrationService @Inject()(repository: UserRepository)(implicit ec: ExecutionContext){

  def register(userCreate: UserCreate): Future[Either[String, UserResponse]] = {
    repository.findByUsername(userCreate.username).onComplete {
      case Success(userOption) => userOption match {
        case Some(user) => println(s"User already exists: $user")
        case _ => println(s"User not found by username [${userCreate.username}]")
      }
    }
    val user = User(username = userCreate.username, passwordHash = "<PASSWORD>", name = userCreate.name)  // TODO hash the password
    repository.create(user).map {
      createdUser => Right(UserResponse(createdUser.id, createdUser.username, createdUser.name))
    }
  }
}
