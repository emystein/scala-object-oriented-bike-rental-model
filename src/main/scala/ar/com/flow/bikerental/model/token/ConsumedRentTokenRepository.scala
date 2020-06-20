package ar.com.flow.bikerental.model.token

import java.util

trait ConsumedRentTokenRepository {
  def save(token: ConsumedRentToken)
  def getAll(): util.Collection[ConsumedRentToken]
  def contains(token: ConsumedRentToken): Boolean
}
