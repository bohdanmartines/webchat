package actor

import actor.WebSocketProtocol.{Authenticate, Authenticated, Error, SendMessage, parseClientMessage, serializeServerMessage}
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
                chatRepository: ChatRepository)
               (implicit ec: ExecutionContext) extends Actor {

  private var userOption: Option[UserResponse] = None

  private var authenticated = false

  override def receive: Receive = {
    case msg: JsValue => {
      parseClientMessage(msg) match {
        case Some(Authenticate(token)) if !authenticated =>
          handleAuthentication(token)
        case Some(Authenticate(_)) if authenticated =>
          out ! serializeServerMessage(Error("Already authenticated"))
        case Some(SendMessage(content)) => println(s"Received a client message '$content'")
        case None => println("Unknown message format")
      }
    }
    case _ => println("Unknown message format")
  }

  private def handleAuthentication(token: String): Unit = {
    jwtService.validateToken(token) match {
      case Some(userId) =>
        val verifyAccessFuture = for {
          userOpt <- userRepository.findById(userId)
          hasAccess <- chatRepository.isUserInChat(chatId, userId)
        } yield (userOpt, hasAccess)

        verifyAccessFuture.onComplete {
          case Success((Some(user), true)) =>
            userOption = Some(UserResponse(userId, user.username))
            authenticated = true
            println(s"User $userId is authenticated for a chat connection")
            out ! serializeServerMessage(Authenticated(success = true))
          case _ =>
            out ! serializeServerMessage(Error("Authentication failed for a chat connection"))
        }

      case None =>
        println("Authentication attempt failed for a chat connection")
        out ! serializeServerMessage(Authenticated(success = false, Some("Invalid token")))
    }
  }
}

object UserActor {
  def props(out: ActorRef,
            chatId: Long,
            jwtService: JwtService,
            userRepository: UserRepository,
            chatRepository: ChatRepository
           )(implicit ec: ExecutionContext): Props = {
    Props(classOf[UserActor], out, chatId, jwtService, userRepository, chatRepository)
  }
}
