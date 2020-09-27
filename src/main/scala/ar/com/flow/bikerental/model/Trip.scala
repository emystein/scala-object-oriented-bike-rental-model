package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now
import java.time.{DayOfWeek, Duration, LocalDateTime}

import ar.com.flow.bikerental.model.token.ConsumedRentToken
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripResult}

trait BikeEvent {
  def user: User
  def bike: Bike
  def timestamp: LocalDateTime
}

case class Trip(var bike: Bike, consumedRentToken: ConsumedRentToken) {
  bike.addTrip(this)

  val pickUp: LocalDateTime = now

  def finish(tripCompletionRules: TripCompletionRules): TripResult = {
    val completedTrip = new FinishedTrip(consumedRentToken.owner, this.bike, this.pickUp, now)
    val rulesCheckResult = tripCompletionRules.test(completedTrip)
    TripResult(completedTrip, rulesCheckResult)
  }
}

class FinishedTrip(val user: User, val bike: Bike, val bikePickUp: LocalDateTime, val bikeDropOff: LocalDateTime) {
  bike.finishTrip()

  def duration: Duration = Duration.between(bikePickUp, bikeDropOff)

  def endDayOfWeek: DayOfWeek = bikeDropOff.getDayOfWeek

  def hasLastedMoreThan(duration: Duration): Boolean = this.duration.compareTo(duration) > 0
}

