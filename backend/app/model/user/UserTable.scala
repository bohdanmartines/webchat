package model.user

import slick.jdbc.MySQLProfile.api._
import slick.lifted.{ProvenShape, Tag}

class UserTable (tag: Tag) extends Table[User](tag, "users"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def passwordHash = column[String]("password_hash")

  override def * : ProvenShape[User] = (id, username, passwordHash) <> (User.tupled, User.unapply)
}
