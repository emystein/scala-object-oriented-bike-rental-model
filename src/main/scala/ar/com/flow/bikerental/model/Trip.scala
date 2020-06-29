package ar.com.flow.bikerental.model

import java.time.{DayOfWeek, Duration, LocalDateTime}
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{ConsumedRentToken, ReservedRentToken}

trait BikeEvent {
  def user: User
  def bike: Bike
  def timestamp: LocalDateTime
}

case class BikePickUpEvent(bike: Bike, consumedToken: ConsumedRentToken) extends BikeEvent {
  val user = consumedToken.owner
  // TODO convert to val
  var timestamp = now
}

case class BikeDropOffEvent(user: User, bike: Bike, timestamp: LocalDateTime) extends BikeEvent

case class Trip(pickUp: BikePickUpEvent)

class FinishedTrip(var bikePickup: BikePickUpEvent, val dropOffTimestamp: LocalDateTime) {
  val user = bikePickup.user
  val bike = bikePickup.bike
  val bikeDropOff = BikeDropOffEvent(bikePickup.user, bikePickup.bike, dropOffTimestamp)

  def duration: Duration = Duration.between(bikePickup.timestamp, bikeDropOff.timestamp)

  def endDayOfWeek: DayOfWeek = bikeDropOff.timestamp.getDayOfWeek

  def hasLastedMoreThan(duration: Duration): Boolean = this.duration.compareTo(duration) > 0
}

