package service

import dto.ChatCreate
import dto.response.{ChatDetails, ChatSummary}
import model.Chat
import play.api.libs.json.Json
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

  def getChat(chatId: Long, userId: Long): Future[ChatDetails] = {
    Future.successful(ChatDetails(1, "Mock Chat", Seq.empty))
  }
}
