package service

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import java.time.Instant
import javax.inject.{Inject, Singleton}

@Singleton
class JwtService @Inject()(config: play.api.Configuration) {

  private val secret = config.get[String]("play.http.secret.key")
  private val tokenLifeTimeSeconds = config.get[Long]("jwt.expiration")
  private val algo = JwtAlgorithm.HS256

  def createToken(userId: Long): String = {
    val claim = JwtClaim(
      subject = Option(userId.toString),
      issuedAt = Some(Instant.now().getEpochSecond),
      expiration = Some(Instant.now().plusSeconds(tokenLifeTimeSeconds).getEpochSecond)
    )
    Jwt.encode(claim, secret, algo)
  }

  def validateToken(token: String): Option[Long] = {
    val triedClaim = Jwt.decode(token, secret, Seq(algo)).toOption
    triedClaim.flatMap(_.subject).map(_.toLong)
  }
}
