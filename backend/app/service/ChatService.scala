package service

import dto.{ChatCreate, ChatResponse}
import model.Chat
import repository.{ChatRepository, UserRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatService @Inject()(repository: ChatRepository)
                           (implicit ec: ExecutionContext) {

  def createChat(chatCreate: ChatCreate, creator: Long): Future[ChatResponse] = {
    repository.create(Chat(name = chatCreate.name, creator = creator))
      .map(c => ChatResponse(c.id, c.name))
  }

  def getChats(userId: Long): Future[Seq[ChatResponse]] = {
    val userChats = repository.findByUser(userId)
    userChats.map(_.map(c => ChatResponse(c.id, c.name)))
  }
}
