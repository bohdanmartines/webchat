package actor

import org.apache.pekko.actor.{Actor, Props}
import play.api.libs.json.JsValue

import scala.concurrent.ExecutionContext

class UserActor extends Actor {
  override def receive: Receive = {
    case msg: JsValue => {
      println(msg)
    }
    case _ => println("Unknown message")
  }
}

object UserActor {
  def props()(implicit ec: ExecutionContext): Props = {
    Props(classOf[UserActor])
  }
}
