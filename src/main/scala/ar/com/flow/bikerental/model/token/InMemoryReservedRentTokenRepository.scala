package ar.com.flow.bikerental.model.token

import java.util

import ar.com.flow.bikerental.model.User
import com.google.common.collect.{ArrayListMultimap, Multimap}

import scala.collection.mutable

class InMemoryReservedRentTokenRepository extends ReservedRentTokenRepository {
  val tokensByValue: mutable.Map[String, ReservedRentToken] = new mutable.HashMap()
  val tokensByUser: Multimap[User, ReservedRentToken] = ArrayListMultimap.create[User, ReservedRentToken]

  override def getByValue(tokenValue: String): Option[ReservedRentToken] = tokensByValue.get(tokenValue)

  override def save(token: ReservedRentToken): Unit = {
    tokensByValue.put(token.value, token)
    tokensByUser.put(token.owner, token)
  }

  override def getAllByUser(user: User): util.Collection[ReservedRentToken] = tokensByUser.get(user)

  override def deleteAll(): Unit = tokensByUser.clear()
}
