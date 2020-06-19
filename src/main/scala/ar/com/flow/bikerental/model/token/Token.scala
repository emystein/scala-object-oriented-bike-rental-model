package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime
import java.time.LocalDateTime.now
import ar.com.flow.bikerental.model.User

abstract class Token

class RentToken(val value: Long = 0L, var expiration: LocalDateTime, val owner: User) extends Token {
  def hasExpired: Boolean = now.isAfter(expiration)

  override def equals(obj: Any): Boolean = obj.isInstanceOf[RentToken] && obj.asInstanceOf[RentToken].value == value

  override def hashCode(): Int = {
    value.toInt
  }
}

case class ReservedRentToken(token: RentToken, user: User, tokenRegistry: TokenRegistry) extends RentToken(token.value, token.expiration, user) {
  val reservedAt: LocalDateTime = now

  def setExpiration(date: LocalDateTime) = expiration = date
  def consume: ConsumedRentToken = tokenRegistry.consumeToken(this)
}

class ConsumedRentToken(val token: ReservedRentToken, val consumedAt: LocalDateTime) extends RentToken(token.value, token.expiration, token.owner)


