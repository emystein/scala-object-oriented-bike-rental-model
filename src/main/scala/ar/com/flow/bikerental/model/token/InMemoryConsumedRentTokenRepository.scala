package ar.com.flow.bikerental.model.token

import java.util

class InMemoryConsumedRentTokenRepository extends ConsumedRentTokenRepository {
  val consumedTokens = new util.HashSet[ConsumedRentToken]

  override def save(token: ConsumedRentToken): Unit = consumedTokens.add(token)

  override def getAll(): util.Collection[ConsumedRentToken] = consumedTokens

  override def contains(token: ConsumedRentToken): Boolean = consumedTokens.contains(token)

  override def deleteAll(): Unit = consumedTokens.clear()
}
