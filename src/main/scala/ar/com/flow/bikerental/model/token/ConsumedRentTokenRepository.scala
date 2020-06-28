package ar.com.flow.bikerental.model.token

trait ConsumedRentTokenRepository {
  def save(token: ConsumedRentToken)
  def getAll(): Iterable[ConsumedRentToken]
  def contains(token: ConsumedRentToken): Boolean
  def deleteAll(): Unit
}
