package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime.now
import java.time.Period
import java.util

import ar.com.flow.bikerental.model.User
import com.google.common.base.Preconditions
import com.google.common.collect.{ArrayListMultimap, Multimap}

case class TokenRegistry(tokenGenerator: TokenGenerator) {
  val consumedTokens = new util.HashSet[Token]
  val tokensByUser: Multimap[User, Token] = ArrayListMultimap.create[User, Token]

  def reserveTokenForUser(user: User): ReservedToken = {
    val token = tokenGenerator.generateTokenValidForPeriod(Period.ofDays(1))
    val reservedToken = new ReservedToken(token, user, this)
    tokensByUser.put(user, reservedToken)
    reservedToken
  }

  def consumeToken(token: ReservedToken) = {
    require(!token.hasExpired, "Can't consume expired token.")
    val consumedToken = new ConsumedToken(token, consumedAt = now())
    require(!consumedTokens.contains(consumedToken), "Can't consume token more than once.")
    consumedTokens.add(consumedToken)
    consumedToken
  }

  def tokensOf(user: User): util.Collection[Token] = tokensByUser.get(user)
}