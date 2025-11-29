package actor

import play.api.libs.json.{Format, JsValue, Json}

object WebSocketProtocol {
  sealed trait ClientMessage
  case class Authenticate(token: String) extends ClientMessage
  case class SendMessage(content: String) extends ClientMessage

  // JSON formats
  implicit val authenticateFormat: Format[Authenticate] = Json.format[Authenticate]
  implicit val sendMessageFormat: Format[SendMessage] = Json.format[SendMessage]

  def parseClientMessage(json: JsValue): Option[ClientMessage] = {
    println(s"Parsing client message $json")
    (json \ "type").asOpt[String] match {
      case Some("authenticate") => json.asOpt[Authenticate]
      case Some("sendMessage") => json.asOpt[SendMessage]
      case _ =>
        println("Message type not recognized")
        None
    }
  }
}
