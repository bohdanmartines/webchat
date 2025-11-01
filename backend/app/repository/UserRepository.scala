package repository

import model.{User, UserTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject._
import scala.concurrent.Future

@Singleton
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                              (implicit ec: scala.concurrent.ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val users = TableQuery[UserTable]

  def create(user: User): Future[User] = {
    val insert = (users returning users.map(_.id) into ((u, id) => u.copy(id = id))) += user
    db.run(insert)
  }

  def findById(id: Long): Future[Option[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
  }

  def findByUsername(username: String): Future[Option[User]] = {
    db.run(users.filter(_.username === username).result.headOption)
  }

}
