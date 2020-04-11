package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.User

case class Token(value: Long = 0L, var expiration: LocalDateTime, owner: User) {
  def hasExpired: Boolean = now.isAfter(expiration)

  override def equals(obj: Any): Boolean = obj.isInstanceOf[Token] && obj.asInstanceOf[Token].value == value

  override def hashCode(): Int = {
    value.toInt
  }
}