package controller

import actor.{ChatManager, UserActor}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, ControllerComponents, WebSocket}
import repository.{ChatRepository, UserRepository}
import service.JwtService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ChatWebSocketController @Inject()(val cc: ControllerComponents,
                                        jwtService: JwtService,
                                        userRepository: UserRepository,
                                        chatRepository: ChatRepository,
                                        charManager: ChatManager)
                                       (implicit system: ActorSystem,
                                         mat: Materializer,
                                         ec: ExecutionContext) extends AbstractController(cc) {

  def socket(chatId: Long): WebSocket = WebSocket.accept[JsValue, JsValue]{ request =>
    println(s"New connection for chat $chatId")
    ActorFlow.actorRef(out => {
      UserActor.props(out, chatId, jwtService, userRepository, chatRepository, charManager.getChatActor)
    })
  }
}
