package actor

import org.apache.pekko.actor.{ActorRef, ActorSystem}

import scala.collection.concurrent.TrieMap

class ChatManager(actorSystem: ActorSystem) {

  private val chatActors = TrieMap.empty[Long, ActorRef]

  def getChatActor(chatId: Long): ActorRef = {
    chatActors.getOrElseUpdate(chatId, actorSystem.actorOf(ChatActor.props(chatId)))
  }

}
