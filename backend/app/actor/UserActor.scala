package actor

import actor.ChatActor.{IncomingMessage, UserConnected, UserDisconnected}
import actor.WebSocketProtocol.{Authenticate, Authenticated, Error, NewMessage, SendMessage, parseClientMessage, serializeServerMessage}
import dto.response.UserResponse
import org.apache.pekko.actor.{Actor, ActorRef, Props}
import play.api.libs.json.JsValue
import repository.{ChatRepository, UserRepository}
import service.JwtService

import scala.concurrent.ExecutionContext
import scala.util.Success

class UserActor(out: ActorRef,
                chatId: Long,
                jwtService: JwtService,
                userRepository: UserRepository,
                chatRepository: ChatRepository,
                getChatActor: Long => ActorRef) extends Actor {

  private implicit val ec: ExecutionContext = context.dispatcher

  private var userOption: Option[UserResponse] = None
  private var chatActorOption: Option[ActorRef] = None

  private var authenticated = false

  override def receive: Receive = {
    case msg: JsValue => {
      parseClientMessage(msg) match {
        case Some(Authenticate(token)) if !authenticated =>
          handleAuthentication(token)
        case Some(Authenticate(_)) if authenticated =>
          out ! serializeServerMessage(Error("Already authenticated"))
        case Some(SendMessage(content)) if authenticated =>
          userOption match {
            case Some(user) =>
              chatActorOption.foreach(_ ! IncomingMessage(user.id, user.username, content))
          }
        case Some(SendMessage(_)) if !authenticated =>
          out ! serializeServerMessage(Error("Non authenticated for the chat connection"))
        case None => println("Unknown message format")
      }
    }
    case msg: NewMessage => out ! serializeServerMessage(msg)
    case _ => println("Unknown message format")
  }

  private def handleAuthentication(token: String): Unit = {
    jwtService.validateToken(token) match {
      case Some(userId) =>
        val verifyAccessFuture = for {
          userOpt <- userRepository.findById(userId)
          chatOpt <- chatRepository.findById(userId)
          hasAccess <- chatRepository.isUserInChat(chatId, userId)
        } yield (userOpt, chatOpt, hasAccess)

        verifyAccessFuture.onComplete {
          case Success((Some(user), Some(_), true)) =>
            userOption = Some(UserResponse(userId, user.username))
            chatActorOption = Some(getChatActor.apply(chatId))
            chatActorOption.foreach(actor => actor ! UserConnected(userId, self))
            authenticated = true
            println(s"User $userId is authenticated for a connection to chat $chatId")
            out ! serializeServerMessage(Authenticated(success = true))
          case _ =>
            out ! serializeServerMessage(Error("Authentication failed for a chat connection"))
        }

      case None =>
        println("Authentication attempt failed for a chat connection")
        out ! serializeServerMessage(Authenticated(success = false, Some("Invalid token")))
    }
  }

  override def postStop(): Unit = {
    for {
      user <- userOption
      chatActor <- chatActorOption
    } chatActor ! UserDisconnected(user.id, self)
  }
}

object UserActor {
  def props(out: ActorRef,
            chatId: Long,
            jwtService: JwtService,
            userRepository: UserRepository,
            chatRepository: ChatRepository,
            getChatActor: Long => ActorRef): Props = {
    Props(new UserActor(out, chatId, jwtService, userRepository, chatRepository, getChatActor))
  }
}
