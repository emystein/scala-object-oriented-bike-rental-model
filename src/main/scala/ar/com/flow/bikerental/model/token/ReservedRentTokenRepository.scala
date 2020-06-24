package ar.com.flow.bikerental.model.token

import java.util

import ar.com.flow.bikerental.model.User

trait ReservedRentTokenRepository {
  def getByValue(tokenId: String): Option[ReservedRentToken]
  def save(token: ReservedRentToken)
  def getAllByUser(user: User): util.Collection[ReservedRentToken]
  def deleteAll(): Unit
}
