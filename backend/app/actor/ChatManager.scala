package actor

import org.apache.pekko.actor.{ActorRef, ActorSystem}
import repository.MessageRepository

import javax.inject.{Inject, Singleton}
import scala.collection.concurrent.TrieMap
import scala.concurrent.ExecutionContext

@Singleton
class ChatManager @Inject() (actorSystem: ActorSystem, messageRepository: MessageRepository)
                            (implicit ec: ExecutionContext) {

  private val chatActors = TrieMap.empty[Long, ActorRef]

  def getChatActor(chatId: Long): ActorRef = {
    chatActors.getOrElseUpdate(chatId, actorSystem.actorOf(ChatActor.props(chatId, messageRepository)))
  }

}
