package ar.com.flow.bikerental.model.token
import java.util

import ar.com.flow.bikerental.model.User
import com.google.common.collect.{ArrayListMultimap, Multimap}

class InMemoryRentTokenRepository extends RentTokenRepository {
  val tokensByUser: Multimap[User, RentToken] = ArrayListMultimap.create[User, RentToken]

  override def save(token: RentToken): Unit = tokensByUser.put(token.owner, token)

  override def getAllByUser(user: User): util.Collection[RentToken] = tokensByUser.get(user)
}
