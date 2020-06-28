package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime.now
import java.time.Period

import ar.com.flow.bikerental.model.User

import scala.util.Random

case class TokenRegistry(random: Random = new Random,
                         reservedTokens: TokenRepository[ReservedRentToken],
                         consumedTokens: TokenRepository[ConsumedRentToken]) {

  def generateTokenValidForPeriod(period: Period, owner: User) = ReservedRentToken(value = random.nextLong().toString, expiration = now.plus(period), owner, this)

  def reserveTokenForUser(user: User): ReservedRentToken = {
    val token = generateTokenValidForPeriod(Period.ofDays(1), user)
    reservedTokens.save(token)
    token
  }

  def consumeToken(token: ReservedRentToken): ConsumedRentToken = {
    require(!token.hasExpired, "Can't consume expired token.")
    val consumedToken = new ConsumedRentToken(token, consumedAt = now())
    require(!consumedTokens.contains(consumedToken), "Can't consume token more than once.")
    consumedTokens.save(consumedToken)
    consumedToken
  }

  def tokensOf(user: User): Iterable[ReservedRentToken] = reservedTokens.getAllByUser(user)

  def deleteAll(): Unit = {
    reservedTokens.clear()
    consumedTokens.clear()
  }
}