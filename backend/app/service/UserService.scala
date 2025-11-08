package service

import com.google.inject.Singleton
import dto.UserResponse
import repository.UserRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject()(repository: UserRepository)
                           (implicit ec: ExecutionContext) {

  def getUser(userId: Long): Future[Option[UserResponse]] = {
    repository.findById(userId).map(
      optionUser => optionUser.map(user => UserResponse(user.id, user.username))
    )
  }
}
