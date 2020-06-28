package ar.com.flow.bikerental.model.token

import ar.com.flow.bikerental.model.User

import scala.collection.mutable

class InMemoryConsumedRentTokenRepository extends TokenRepository[ConsumedRentToken] {
  private val consumedTokens = new mutable.HashSet[ConsumedRentToken]

  override def save(token: ConsumedRentToken): Unit = consumedTokens.add(token)

  override def getAll(): Iterable[ConsumedRentToken] = consumedTokens

  override def getById(id: String): Option[ConsumedRentToken] = consumedTokens.find(t => t.value == id)

  override def getAllByUser(user: User): Iterable[ConsumedRentToken] = consumedTokens.filter(_.owner == user)

  override def contains(token: ConsumedRentToken): Boolean = consumedTokens.contains(token)

  override def clear(): Unit = consumedTokens.clear()
}
