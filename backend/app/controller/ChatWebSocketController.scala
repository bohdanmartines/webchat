package controller

import actor.UserActor
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, ControllerComponents, WebSocket}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ChatWebSocketController @Inject()(val cc: ControllerComponents)
                                       (implicit system: ActorSystem,
                                         mat: Materializer,
                                         ec: ExecutionContext) extends AbstractController(cc) {
  def socket(chatId: Long): WebSocket = WebSocket.accept[JsValue, JsValue]{ request =>
    ActorFlow.actorRef(out => {
      UserActor.props()
    })
  }
}
