package ar.com.flow.bikerental.model.token

import ar.com.flow.bikerental.model.User

trait TokenRepository[T] {
  def save(token: T)
  def getAll(): Iterable[T]
  def getById(tokenId: String): Option[T]
  def getAllByUser(user: User): Iterable[T]
  def contains(token: T): Boolean
  def clear(): Unit
}
