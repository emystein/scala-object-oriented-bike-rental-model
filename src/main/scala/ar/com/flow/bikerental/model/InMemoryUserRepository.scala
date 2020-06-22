package ar.com.flow.bikerental.model

import scala.collection.mutable

class InMemoryUserRepository extends UserRepository {
  val usersById: mutable.Map[String, User] = new mutable.HashMap[String, User]

  override def save(user: User): Unit = usersById.put(user.id, user)

  override def getById(userId: String): Option[User] = usersById.get(userId)
}
