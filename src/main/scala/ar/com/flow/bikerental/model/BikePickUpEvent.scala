package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{ConsumedRentToken, ReservedRentToken}

case class BikePickUpEvent(bike: Bike, reservedToken: ReservedRentToken) extends BikeEvent {
  val consumedToken: ConsumedRentToken = reservedToken.consume
  val user = consumedToken.owner
  // TODO convert to val
  var timestamp = now
}