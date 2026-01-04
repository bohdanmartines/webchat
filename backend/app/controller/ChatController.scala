package controller

import dto.{ChatCreate, ParticipantRequest}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repository.{ChatRepository, UserRepository}
import service.ChatService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatController @Inject()(val controllerComponents: ControllerComponents,
                               chatService: ChatService,
                               chatRepository: ChatRepository,
                               userRepository: UserRepository,
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

  def addParticipant(chatId: Long): Action[JsValue] = securedActionFactory.async(parse.json) { request =>
    val participantRequest = request.body.validate[ParticipantRequest]
    participantRequest.fold(
      errors => Future.successful(BadRequest(errors.toString)),
      pr => {
        println(s"Adding user ${pr.username} to chat $chatId")
        chatRepository.isChatOwner(chatId, request.userId).flatMap {
          case false => Future.successful(Forbidden("You are not chat owner"))
          case true =>
            userRepository.findByUsername(pr.username).flatMap {
              case None => Future.successful(BadRequest(Json.obj("error" -> s"User ${pr.username} not found")))
              case Some(user) => chatService.addParticipant(chatId, user.id).map { _ => Ok}
            }
        }
      }
    )
  }

  def removeParticipant(chatId: Long): Action[JsValue] = securedActionFactory.async(parse.json) { request =>
    val participantRequest = request.body.validate[ParticipantRequest]
    participantRequest.fold(
      errors => Future.successful(BadRequest(errors.toString)),
      pr => {
        println(s"Removing user ${pr.username} from chat $chatId")
        chatRepository.isChatOwner(chatId, request.userId).flatMap {
          case false => Future.successful(Forbidden("You are not chat owner"))
          case true =>
            userRepository.findByUsername(pr.username).flatMap {
              case None => Future.successful(BadRequest(Json.obj("error" -> s"User ${pr.username} not found")))
              case Some(user) => chatService.removeParticipant(chatId, user.id).map { _ => Ok}
            }
        }
      }
    )
  }
}
