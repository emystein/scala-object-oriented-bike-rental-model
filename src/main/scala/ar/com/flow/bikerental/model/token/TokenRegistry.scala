package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime.now
import java.time.Period
import java.util

import ar.com.flow.bikerental.model.User

case class TokenRegistry(tokenGenerator: TokenGenerator,
                         tokensByUser: RentTokenRepository,
                         consumedTokens: ConsumedRentTokenRepository) {
  def reserveTokenForUser(user: User): ReservedRentToken = {
    val token = tokenGenerator.generateTokenValidForPeriod(Period.ofDays(1))
    val reservedToken = new ReservedRentToken(token, user, this)
    tokensByUser.save(reservedToken)
    reservedToken
  }

  def consumeToken(token: ReservedRentToken): ConsumedRentToken = {
    require(!token.hasExpired, "Can't consume expired token.")
    val consumedToken = new ConsumedRentToken(token, consumedAt = now())
    require(!consumedTokens.contains(consumedToken), "Can't consume token more than once.")
    consumedTokens.save(consumedToken)
    consumedToken
  }

  def tokensOf(user: User): util.Collection[RentToken] = tokensByUser.getAllByUser(user)

  def deleteAll(): Unit = {
    tokensByUser.deleteAll()
    consumedTokens.deleteAll()
  }
}