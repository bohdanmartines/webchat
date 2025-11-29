package actor

import actor.WebSocketProtocol.{Authenticate, SendMessage, parseClientMessage}
import org.apache.pekko.actor.{Actor, Props}
import play.api.libs.json.JsValue

import scala.concurrent.ExecutionContext

class UserActor extends Actor {

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

  private def handleAuthentication(token: String) = {
    println("Authenticating a user")
    // TODO Verify the token
    authenticated = true
  }
}

object UserActor {
  def props()(implicit ec: ExecutionContext): Props = {
    Props(classOf[UserActor])
  }
}
