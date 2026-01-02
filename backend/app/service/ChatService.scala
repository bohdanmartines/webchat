package service

import actor.WebSocketProtocol.NewMessage
import dto.ChatCreate
import dto.response.{ChatDetails, ChatSummary, UserResponse}
import model.chat.Chat
import repository.{ChatRepository, MessageRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
class ChatService @Inject()(repository: ChatRepository, messageRepository: MessageRepository)
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
        case true => repository.findDetailsById(chatId)
          .map(_.map(c => ChatDetails(
            c.id,
            c.name,
            c.participants.map(user => UserResponse(user.id, user.username))
          )))
        case false => Future.successful(None)
      }
  }

  def getMessages(chatId: Long, userId: Long): Future[Seq[NewMessage]] = {
    repository.isUserInChat(chatId, userId)
      .flatMap {
        case true => messageRepository.getMessages(chatId)
        case false => Future.successful(List.empty)
      }
  }

  def addParticipant(chatId: Long, userIdToAdd: Long): Future[Unit] = {
    repository.isUserInChat(chatId, userIdToAdd).flatMap {
      case false => repository.addParticipant(chatId, userIdToAdd)
      case true => Future.successful()
    }
  }
}
