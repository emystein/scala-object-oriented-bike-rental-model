package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.User

trait Token {
  val value: String = "0"
  var expiration: LocalDateTime
  val owner: User
}

case class ReservedRentToken(override val value: String = "0", override var expiration: LocalDateTime, override val owner: User, tokenRegistry: TokenRegistry) extends Token {
  val reservedAt: LocalDateTime = now
  def setExpiration(dateTime: LocalDateTime): Unit = expiration = dateTime
  def hasExpired: Boolean = now.isAfter(expiration)
  def consume: ConsumedRentToken = tokenRegistry.consumeToken(this)
  override def equals(obj: Any): Boolean = obj.isInstanceOf[ReservedRentToken] && obj.asInstanceOf[ReservedRentToken].value == value
  override def hashCode(): Int = {
    value.hashCode
  }
}

class ConsumedRentToken(val token: ReservedRentToken, val consumedAt: LocalDateTime) extends Token {
  override val value: String = token.value
  override var expiration: LocalDateTime = token.expiration
  override val owner: User = token.owner
  override def equals(obj: Any): Boolean = obj.isInstanceOf[ConsumedRentToken] && obj.asInstanceOf[ConsumedRentToken].value == value
  override def hashCode(): Int = {
    value.hashCode
  }
}
