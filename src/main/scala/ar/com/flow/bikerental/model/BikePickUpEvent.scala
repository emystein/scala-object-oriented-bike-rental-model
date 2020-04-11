package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{ConsumedToken, ReservedToken}

case class BikePickUpEvent(bike: Bike, reservedToken: ReservedToken) extends BikeEvent {
  val consumedToken: ConsumedToken = reservedToken.consume
  val user = consumedToken.owner
  // TODO convert to val
  var timestamp = now
}