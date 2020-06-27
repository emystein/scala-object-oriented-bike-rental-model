package ar.com.flow.bikerental.model

trait UserRepository {
  def save(user: User): User
  def getById(userId: Long): Option[User]
}
