package ar.com.flow.bikerental.model

trait UserRepository {
  def save(user: User): Unit
  def getById(userId: String): Option[User]
}
