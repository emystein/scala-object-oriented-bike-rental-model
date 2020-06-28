package ar.com.flow.bikerental.model.token

import ar.com.flow.bikerental.model.User

import scala.collection.mutable

class InMemoryReservedRentTokenRepository extends TokenRepository[ReservedRentToken] {
  val tokensById: mutable.Map[String, ReservedRentToken] = new mutable.HashMap()

  override def getAll(): Iterable[ReservedRentToken] = tokensById.values

  override def getById(id: String): Option[ReservedRentToken] = tokensById.get(id)

  override def save(token: ReservedRentToken): Unit = {
    tokensById.put(token.value, token)
  }

  override def getAllByUser(user: User): Iterable[ReservedRentToken] = tokensById.values.filter(_.owner == user)

  override def contains(token: ReservedRentToken): Boolean = tokensById.valuesIterator.contains(token)

  override def clear(): Unit = tokensById.clear()
}
