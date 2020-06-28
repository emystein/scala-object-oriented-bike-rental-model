package ar.com.flow.bikerental.model.token

import scala.collection.mutable

class InMemoryConsumedRentTokenRepository extends ConsumedRentTokenRepository {
  private val consumedTokens = new mutable.HashSet[ConsumedRentToken]

  override def save(token: ConsumedRentToken): Unit = consumedTokens.add(token)

  override def getAll(): Iterable[ConsumedRentToken] = consumedTokens

  override def contains(token: ConsumedRentToken): Boolean = consumedTokens.contains(token)

  override def deleteAll(): Unit = consumedTokens.clear()
}
