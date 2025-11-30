package actor

import actor.WebSocketProtocol.NewMessage
import org.apache.pekko.actor.{Actor, ActorRef, Props}

import scala.collection.concurrent.TrieMap

class ChatActor(chatId: Long) extends Actor {
  import actor.ChatActor._

  private val userActors = TrieMap.empty[Long, ActorRef]

  override def receive: Receive = {
    case UserConnected(userId, userActor) =>
      userActors += (userId -> userActor)
      println(s"User $userId connected to chat $chatId. Current users in the chat are $userActors")
    case UserDisconnected(userId) =>
      userActors -= userId
      println(s"User $userId disconnected from chat $chatId. Current users in the chat are $userActors")
    case IncomingMessage(userId, username, content) =>
      userActors.foreach(
        entry => entry._2 ! NewMessage(0, userId, username, content)
      )
  }
}

object ChatActor {

  case class UserConnected(userId: Long, userActor: ActorRef)
  case class UserDisconnected(userId: Long)
  case class IncomingMessage(userId: Long, username: String, content: String)

  def props(chatId: Long): Props = {
    Props(new ChatActor(chatId))
  }
}


