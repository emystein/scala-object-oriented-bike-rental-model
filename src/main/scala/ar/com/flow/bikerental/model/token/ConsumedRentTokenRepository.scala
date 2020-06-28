package ar.com.flow.bikerental.model.token

import ar.com.flow.bikerental.model.User

trait ConsumedRentTokenRepository {
  def save(token: ConsumedRentToken)
  def getAll(): Iterable[ConsumedRentToken]
  def getById(id: String): Option[ConsumedRentToken]
  def getAllByUser(user: User): Iterable[ConsumedRentToken]
  def contains(token: ConsumedRentToken): Boolean
  def deleteAll(): Unit
}
