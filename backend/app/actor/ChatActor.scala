package actor

import actor.WebSocketProtocol.NewMessage
import org.apache.pekko.actor.{Actor, ActorRef, Props}

import scala.collection.concurrent.TrieMap

class ChatActor(chatId: Long) extends Actor {
  import actor.ChatActor._

  private val userActors = TrieMap.empty[Long, Set[ActorRef]]

  override def receive: Receive = {
    case UserConnected(userId, userActor) =>
      val currentConnections = userActors.getOrElse(userId, Set.empty)
      val updatedConnections = currentConnections + userActor
      userActors += (userId -> updatedConnections)
      println(s"User $userId connected to chat $chatId. Current users in the chat are $userActors")

    case UserDisconnected(userId, userActor) =>
      val currentConnections = userActors.get(userId)
      currentConnections.foreach { connections =>
        if (connections.size > 1) {
          val updatedConnections = connections - userActor
          userActors += (userId -> updatedConnections)
        } else {
          userActors -= userId
        }
      }
      println(s"User $userId disconnected from chat $chatId. Current users in the chat are $userActors")

    case IncomingMessage(userId, username, content) =>
      userActors.values.foreach( connections =>
        connections.foreach(_ ! NewMessage(0, userId, username, content))
      )
  }
}

object ChatActor {

  case class UserConnected(userId: Long, userActor: ActorRef)
  case class UserDisconnected(userId: Long, userActor: ActorRef)
  case class IncomingMessage(userId: Long, username: String, content: String)

  def props(chatId: Long): Props = {
    Props(new ChatActor(chatId))
  }
}


