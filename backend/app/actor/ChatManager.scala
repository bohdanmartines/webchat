package actor

import org.apache.pekko.actor.{ActorRef, ActorSystem}

import javax.inject.Inject
import scala.collection.concurrent.TrieMap

class ChatManager @Inject() (actorSystem: ActorSystem) {

  private val chatActors = TrieMap.empty[Long, ActorRef]

  def getChatActor(chatId: Long): ActorRef = {
    chatActors.getOrElseUpdate(chatId, actorSystem.actorOf(ChatActor.props(chatId)))
  }

}
