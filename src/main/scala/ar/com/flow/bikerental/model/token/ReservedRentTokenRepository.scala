package ar.com.flow.bikerental.model.token

import java.util

import ar.com.flow.bikerental.model.User

trait ReservedRentTokenRepository {
  def save(token: ReservedRentToken)
  def getAll(): Iterable[ReservedRentToken]
  def getById(tokenId: String): Option[ReservedRentToken]
  def getAllByUser(user: User): Iterable[ReservedRentToken]
  def contains(token: ReservedRentToken): Boolean
  def deleteAll(): Unit
}
