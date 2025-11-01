package service

import dto.{UserCreate, UserResponse}
import repository.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
class RegistrationService @Inject()(repository: UserRepository)(implicit ec: ExecutionContext){

  def register(user: UserCreate): Future[Either[String, UserResponse]] = {
    repository.findById(1).onComplete(userById => userById match {
      case Success(user) => println(s"User found: $user")
      case _ => println("User not found")
    })
    Future.successful(Right(UserResponse(id = 1, username = user.username, name = "MOCK USER from Service")))
  }
}
