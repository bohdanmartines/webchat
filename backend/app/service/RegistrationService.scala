package service

import dto.{UserCreate, UserResponse}
import repository.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
class RegistrationService @Inject()(repository: UserRepository)(implicit ec: ExecutionContext){

  def register(user: UserCreate): Future[Either[String, UserResponse]] = {
    repository.findByUsername(user.username).onComplete {
      case Success(userOption) => userOption match {
        case Some(user) => println(s"User already exists: $user")
        case _ => println(s"User not found by username [${user.username}]")
      }
    }
    Future.successful(Right(UserResponse(id = 1, username = user.username, name = "MOCK USER from Service")))
  }
}
