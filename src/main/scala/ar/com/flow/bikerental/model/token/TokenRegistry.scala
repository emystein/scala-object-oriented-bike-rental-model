package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime.now
import java.time.Period
import java.util

import ar.com.flow.bikerental.model.User

import scala.util.Random

case class TokenRegistry(random: Random = new Random,
                         tokensByUser: ReservedRentTokenRepository,
                         consumedTokens: ConsumedRentTokenRepository) {

  def generateTokenValidForPeriod(period: Period, owner: User) = ReservedRentToken(value = random.nextString(30), expiration = now.plus(period), owner, this)

  def reserveTokenForUser(user: User): ReservedRentToken = {
    val reservedToken = generateTokenValidForPeriod(Period.ofDays(1), user)
    tokensByUser.save(reservedToken)
    reservedToken
  }

  def getTokenByValue(value: String): Option[ReservedRentToken] = {
    tokensByUser.getByValue(value)
  }

  def consumeToken(token: ReservedRentToken): ConsumedRentToken = {
    require(!token.hasExpired, "Can't consume expired token.")
    val consumedToken = new ConsumedRentToken(token, consumedAt = now())
    require(!consumedTokens.contains(consumedToken), "Can't consume token more than once.")
    consumedTokens.save(consumedToken)
    consumedToken
  }

  def tokensOf(user: User): util.Collection[ReservedRentToken] = tokensByUser.getAllByUser(user)

  def deleteAll(): Unit = {
    tokensByUser.deleteAll()
    consumedTokens.deleteAll()
  }
}