package controller

import dto.ChatCreate
import play.api.libs.json.Json
import play.api.mvc._
import repository.ChatRepository
import service.ChatService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatController @Inject()(val controllerComponents: ControllerComponents,
                               chatService: ChatService,
                               chatRepository: ChatRepository,
                               securedActionFactory: SecuredAction)
                              (implicit ec: ExecutionContext) extends BaseController {

  def createChat() = securedActionFactory.async(parse.json) { request =>
    val chatCreate = request.body.validate[ChatCreate]
    chatCreate.fold(
      errors => Future.successful(BadRequest(errors.toString)),
      chat => chatService.createChat(chat, request.userId).map {c => Created(Json.toJson(c))}
    )
  }

  def getChats() = securedActionFactory.async { request =>
    chatService.getChats(request.userId)
      .map(chats => Ok(Json.toJson(chats)))
  }

  def getChat(chatId: Long): Action[AnyContent] = securedActionFactory.async { request =>
    chatService.getChat(chatId, request.userId)
      .map {
        case Some(chat) => Ok(Json.toJson(chat))
        case None => NotFound(Json.obj("error" -> "Chat not found"))
      }
  }

  def getMessages(chatId: Long): Action[AnyContent] = securedActionFactory.async { request =>
    println(s"Getting messages for chat $chatId")
    chatService.getMessages(chatId, request.userId)
      .map { msgs => Ok(Json.toJson(msgs))}
  }

  def addParticipant(chatId: Long, userId: Long): Action[AnyContent] = securedActionFactory.async { request =>
    println(s"Adding user $userId to chat $chatId")
    chatRepository.isChatOwner(chatId, request.userId).flatMap {
      case true => chatService.addParticipant(chatId, userId).map { _ => Ok}
      case false => Future.successful(Forbidden("You are not chat owner"))
    }
  }

  def removeParticipant(chatId: Long, userId: Long): Action[AnyContent] = securedActionFactory.async { request =>
    println(s"Removing user $userId from chat $chatId")
    chatRepository.isChatOwner(chatId, request.userId).flatMap {
      case true => chatService.removeParticipant(chatId, userId).map { _ => Ok}
      case false => Future.successful(Forbidden("You are not chat owner"))
    }
  }
}
