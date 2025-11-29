package actor

import org.apache.pekko.actor.Props

import scala.concurrent.ExecutionContext

class UserActor {}

object UserActor {
  def props()(implicit ec: ExecutionContext): Props = {
    Props(classOf[UserActor])
  }
}
