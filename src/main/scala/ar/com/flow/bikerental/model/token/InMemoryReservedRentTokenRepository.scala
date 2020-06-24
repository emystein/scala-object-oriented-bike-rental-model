package ar.com.flow.bikerental.model.token

import java.util

import ar.com.flow.bikerental.model.User
import com.google.common.collect.{ArrayListMultimap, Multimap}

import scala.collection.mutable

class InMemoryReservedRentTokenRepository extends ReservedRentTokenRepository {
  val tokensById: mutable.Map[String, ReservedRentToken] = new mutable.HashMap()
  val tokensByUser: Multimap[User, ReservedRentToken] = ArrayListMultimap.create[User, ReservedRentToken]

  override def getByValue(tokenId: String): Option[ReservedRentToken] = tokensById.get(tokenId)

  override def save(token: ReservedRentToken): Unit = {
    tokensById.put(token.value, token)
    tokensByUser.put(token.owner, token)
  }

  override def getAllByUser(user: User): util.Collection[ReservedRentToken] = tokensByUser.get(user)

  override def deleteAll(): Unit = tokensByUser.clear()
}
