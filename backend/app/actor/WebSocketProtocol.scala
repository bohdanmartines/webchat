package actor

import play.api.libs.json.{Format, JsValue, Json}

object WebSocketProtocol {
  sealed trait ClientMessage
  case class SendMessage(content: String) extends ClientMessage

  // JSON formats
  implicit val sendMessageFormat: Format[SendMessage] = Json.format[SendMessage]

  def parseClientMessage(json: JsValue): Option[ClientMessage] = {
    println(s"Parsing client message $json")
    (json \ "type").asOpt[String] match {
      case Some("sendMessage") => json.asOpt[SendMessage]
      case _ => None
    }
  }
}
