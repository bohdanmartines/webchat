package actor

import play.api.libs.json.{Format, JsObject, JsValue, Json}

object WebSocketProtocol {

  private val MessageTypeField = "type"

  sealed trait ClientMessage
  case class Authenticate(token: String) extends ClientMessage
  case class SendMessage(content: String) extends ClientMessage

  sealed trait ServerMessage
  case class Authenticated(success: Boolean, error: Option[String] = None) extends ServerMessage
  case class NewMessage(id: Long, userId: Long, username: String, content: String) extends ServerMessage
  case class Error(error: String) extends ServerMessage

  // JSON formats
  implicit val authenticateFormat: Format[Authenticate] = Json.format[Authenticate]
  implicit val sendMessageFormat: Format[SendMessage] = Json.format[SendMessage]

  implicit val authenticatedFormat: Format[Authenticated] = Json.format[Authenticated]
  implicit val newMessageFormat: Format[NewMessage] = Json.format[NewMessage]
  implicit val errorFormat: Format[Error] = Json.format[Error]

  def parseClientMessage(json: JsValue): Option[ClientMessage] = {
    println(s"Parsing client message $json")
    (json \ MessageTypeField).asOpt[String] match {
      case Some("authenticate") => json.asOpt[Authenticate]
      case Some("sendMessage") => json.asOpt[SendMessage]
      case _ =>
        println("Message type not recognized")
        None
    }
  }

  def serializeServerMessage(message: ServerMessage): JsValue = {
    message match {
      case m: Authenticated =>
        Json.obj(MessageTypeField -> "authenticated") ++ Json.toJson(m).as[JsObject]
      case m: NewMessage =>
        Json.obj(MessageTypeField -> "newMessage") ++ Json.toJson(m).as[JsObject]
      case m: Error =>
        Json.obj(MessageTypeField -> "error") ++ Json.toJson(m).as[JsObject]
    }
  }
}
