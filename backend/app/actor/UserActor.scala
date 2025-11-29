package actor

import actor.WebSocketProtocol.{Authenticate, SendMessage, parseClientMessage}
import org.apache.pekko.actor.{Actor, Props}
import play.api.libs.json.JsValue

import scala.concurrent.ExecutionContext

class UserActor extends Actor {
  override def receive: Receive = {
    case msg: JsValue => {
      parseClientMessage(msg) match {
        case Some(Authenticate(_)) => println(s"Received an authenticate message")
        case Some(SendMessage(content)) => println(s"Received a client message '$content'")
        case None => println("Unknown message format")
      }
    }
    case _ => println("Unknown message format")
  }
}

object UserActor {
  def props()(implicit ec: ExecutionContext): Props = {
    Props(classOf[UserActor])
  }
}
