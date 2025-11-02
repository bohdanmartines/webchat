package controller

import play.api.libs.json.Json
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, BodyParsers, Request, Result, Results, WrappedRequest}
import service.JwtService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

class SecuredRequest[A](val userId: Long, request: Request[A]) extends WrappedRequest[A](request)

@Singleton
class SecuredAction @Inject()(parser: BodyParsers.Default, jwtService: JwtService)
                             (implicit ec: ExecutionContext) extends ActionBuilder[SecuredRequest, AnyContent] {

  override def parser: BodyParser[AnyContent] = parser

  override protected def executionContext: ExecutionContext = ec

  override def invokeBlock[A](request: Request[A], block: SecuredRequest[A] => Future[Result]): Future[Result] = {
    val authHeader = request.headers.get("Authorization").flatMap(_.split(" ").lift(1))
    authHeader match {
      case Some(token) =>
        jwtService.validateToken(token) match {
          case Some(userId) => block(new SecuredRequest(userId, request))
          case None => Future.successful(Results.Unauthorized(Json.obj("error" -> "Invalid token")))
        }
      case None => Future.successful(Results.Unauthorized(Json.obj("error" -> "Missing token")))
    }
  }
}
