package ar.com.flow.bikerental.model

import java.time.{DayOfWeek, Duration, LocalDateTime}
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{ConsumedRentToken, ReservedRentToken}
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripResult}

trait BikeEvent {
  def user: User
  def bike: Bike
  def timestamp: LocalDateTime
}

case class BikePickUpEvent(var bike: Bike, consumedToken: ConsumedRentToken) extends BikeEvent {
  val user = consumedToken.owner
  // TODO convert to val
  var timestamp = now
}

case class BikeDropOffEvent(user: User, bike: Bike, timestamp: LocalDateTime) extends BikeEvent

case class Trip(var bike: Bike, consumedRentToken: ConsumedRentToken) {
  bike.addTrip(this)
  val pickUp: BikePickUpEvent = BikePickUpEvent(bike, consumedRentToken)

  def finish(tripCompletionRules: TripCompletionRules): TripResult = {
    val completedTrip = new FinishedTrip(consumedRentToken.owner, this.bike, this.pickUp.timestamp, now)
    val rulesCheckResult = tripCompletionRules.test(completedTrip)
    TripResult(completedTrip, rulesCheckResult)
  }
}

class FinishedTrip(val user: User, val bike: Bike, val bikePickup: LocalDateTime, val dropOffTimestamp: LocalDateTime) {
  bike.finishTrip()
  val bikeDropOff = BikeDropOffEvent(user, bike, dropOffTimestamp)

  def duration: Duration = Duration.between(bikePickup, bikeDropOff.timestamp)

  def endDayOfWeek: DayOfWeek = bikeDropOff.timestamp.getDayOfWeek

  def hasLastedMoreThan(duration: Duration): Boolean = this.duration.compareTo(duration) > 0
}

