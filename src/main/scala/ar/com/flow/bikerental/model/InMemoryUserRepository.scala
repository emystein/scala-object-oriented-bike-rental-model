package ar.com.flow.bikerental.model

import java.util.UUID

import scala.collection.mutable

class InMemoryUserRepository extends UserRepository {
  val usersById: mutable.Map[String, User] = new mutable.HashMap[String, User]

  override def save(user: User): User = {
    val saveUser = User(Some(user.id.getOrElse(UUID.randomUUID().toString)), user.name)

    usersById.put(saveUser.id.get, saveUser)

    saveUser
  }

  override def getById(userId: String): Option[User] = usersById.get(userId)
}
