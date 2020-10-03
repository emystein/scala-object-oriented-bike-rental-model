package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now
import java.time.{LocalDateTime, Period}

import scala.collection.mutable

case class User(id: Option[Long] = None, name: String) {
  var ban: Option[UserBan] = None

  def isBanned = ban.isDefined

  def ban(banPeriod: Period): Unit = ban = Some(UserBan(this, now, banPeriod))
}

case class UserBan(bannedUser: User, beginningOfBan: LocalDateTime, banPeriod: Period)

trait UserRepository {
  def save(user: User): User
  def getById(userId: Long): Option[User]
}

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
