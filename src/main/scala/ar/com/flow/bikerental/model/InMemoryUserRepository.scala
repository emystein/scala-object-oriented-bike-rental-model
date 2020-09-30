package ar.com.flow.bikerental.model

import scala.collection.mutable

class InMemoryUserRepository extends UserRepository {
  val usersById: mutable.Map[Long, User] = new mutable.HashMap[Long, User]

  override def save(user: User): User = {
    val saveUser = User(Some(user.id.getOrElse(nextId())), user.name)

    usersById.put(saveUser.id.get, saveUser)

    saveUser
  }

  override def getById(userId: Long): Option[User] = usersById.get(userId)

  private def nextId() = usersById.keys.max + 1
}
