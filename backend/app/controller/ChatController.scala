package controller

import dto.ChatCreate
import play.api.libs.json.Json
import play.api.mvc._
import service.ChatService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatController @Inject()(val controllerComponents: ControllerComponents,
                              chatService: ChatService,
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

  def getChat(chatId: Long) = securedActionFactory.async { request =>
    Future.successful(Ok(Json.obj("message" -> s"Chat details for $chatId")))
  }
}
