package service

import dto.ChatCreate
import dto.response.{ChatDetails, ChatSummary, UserResponse}
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

  def getChat(chatId: Long, userId: Long): Future[Option[ChatDetails]] = {
    repository.isUserInChat(chatId, userId)
      .flatMap {
        case true => repository.findByIdAndUser(chatId)
          .map(_.map(c => ChatDetails(
            c.id,
            c.name,
            c.participants.map(user => UserResponse(user.id, user.username))
          )))
        case false => Future.successful(None)
      }
  }
}
