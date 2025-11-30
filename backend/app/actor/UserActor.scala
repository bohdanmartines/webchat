package actor

import actor.WebSocketProtocol.{Authenticate, Authenticated, SendMessage, parseClientMessage, serializeServerMessage}
import dto.response.UserResponse
import org.apache.pekko.actor.{Actor, ActorRef, Props}
import play.api.libs.json.JsValue
import service.JwtService

import scala.concurrent.ExecutionContext

class UserActor(out: ActorRef, jwtService: JwtService) extends Actor {

  private var userOption: Option[UserResponse] = None

  private var authenticated = false

  override def receive: Receive = {
    case msg: JsValue => {
      parseClientMessage(msg) match {
        case Some(Authenticate(token)) if !authenticated =>
          println(s"Received an authenticate message")
          handleAuthentication(token)
        case Some(SendMessage(content)) => println(s"Received a client message '$content'")
        case None => println("Unknown message format")
      }
    }
    case _ => println("Unknown message format")
  }

  private def handleAuthentication(token: String): Unit = {
    jwtService.validateToken(token) match {
      case Some(userId) =>
        userOption = Some(UserResponse(userId, "MOCK_NAME"))
        authenticated = true
        println(s"User $userId is authenticated for a chat connection")
        out ! serializeServerMessage(Authenticated(success = true))
      case None =>
        println("Authentication attempt failed for a chat connection")
        out ! serializeServerMessage(Authenticated(success = false, Some("Invalid token")))
    }
  }
}

object UserActor {
  def props(out: ActorRef,
            jwtService: JwtService
           )(implicit ec: ExecutionContext): Props = {
    Props(classOf[UserActor], out, jwtService)
  }
}
