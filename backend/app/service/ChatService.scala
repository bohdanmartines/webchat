package service

import dto.ChatCreate
import dto.response.ChatSummary
import model.Chat
import repository.{ChatRepository, UserRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatService @Inject()(repository: ChatRepository)
                           (implicit ec: ExecutionContext) {

  def createChat(chatCreate: ChatCreate, creator: Long): Future[ChatSummary] = {
    repository.create(Chat(name = chatCreate.name, creator = creator))
      .map(c => ChatSummary(c.id, c.name, c.participantCount))
  }

  def getChats(userId: Long): Future[Seq[ChatSummary]] = {
    val userChats = repository.findByUser(userId)
    userChats.map(_.map(c => ChatSummary(c.id, c.name, c.participantCount)))
  }
}
