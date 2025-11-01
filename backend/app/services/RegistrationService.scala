package services

import models.{User, UserCreate}

import javax.inject.Singleton

@Singleton
class RegistrationService {

  def register(user: UserCreate): User = {
    User(id = 1, username = user.email, passwordHash = "<PASSWORD>", name = "MOCK USER")
  }
}
