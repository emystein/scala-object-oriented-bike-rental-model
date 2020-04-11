package ar.com.flow.bikerental.model.token

import java.time.{LocalDate, LocalDateTime}
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.User

class ReservedToken(token: Token, user: User, tokenRegistry: TokenRegistry) extends Token(token.value, token.expiration, user) {
  val reservedAt: LocalDateTime = now

  def setExpiration(date: LocalDateTime) = expiration = date
  def consume: ConsumedToken = tokenRegistry.consumeToken(this)
}