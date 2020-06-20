package ar.com.flow.bikerental.model.token

import java.util

import ar.com.flow.bikerental.model.User

trait RentTokenRepository {
  def save(token: RentToken)
  def getAllByUser(user: User): util.Collection[RentToken]
  def deleteAll(): Unit
}
