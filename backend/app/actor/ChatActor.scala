package actor

import org.apache.pekko.actor.{Actor, Props}

class ChatActor(chatId: Long) extends Actor {

  override def receive: Receive = {
    value => println(s"Chat $chatId received message `$value`")
  }
}

object ChatActor {
  def props(chatId: Long): Props = {
    Props(new ChatActor(chatId))
  }
}


