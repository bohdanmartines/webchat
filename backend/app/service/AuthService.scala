package service

import dto.{Login, UserCreate, UserResponse}
import model.User
import org.mindrot.jbcrypt.BCrypt
import repository.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthService @Inject()(repository: UserRepository, jwtService: JwtService)
                           (implicit ec: ExecutionContext) {

  def register(userCreate: UserCreate): Future[Either[String, UserResponse]] = {
    repository.findByUsername(userCreate.username).flatMap {
      case Some(existing) => Future.successful(Left(s"Username [${existing.username}] already exists"))
      case None =>
        println(s"About to create user: [${userCreate.username}]")
        val hash = BCrypt.hashpw(userCreate.password, BCrypt.gensalt())
        val user = User(username = userCreate.username, passwordHash = hash, name = userCreate.name)
        repository.create(user).map {
          createdUser => Right(UserResponse(createdUser.id, createdUser.username, createdUser.name))
        }
    }
  }

  def login(request: Login): Future[Either[String, String]] = {
    println(s"Login request for: [${request.username}]")
    repository.findByUsername(request.username).flatMap {
      case Some(user) if BCrypt.checkpw(request.password, user.passwordHash) =>
          Future.successful(Right(jwtService.createToken(user.id)))
      case _ => Future.successful(Left("Invalid credentials"))
    }
  }
}
