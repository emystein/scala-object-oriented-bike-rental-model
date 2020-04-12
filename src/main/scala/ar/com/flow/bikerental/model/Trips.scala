package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.ReservedToken
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripResult}

import scala.collection.mutable

case class Trips(tripCompletionRules: TripCompletionRules) {
  private val tripsByBike = new mutable.HashMap[Bike, Trip]

  def startTrip(bike: Bike, reservedToken: ReservedToken): Trip = {
    val pickUp = BikePickUpEvent(bike, reservedToken)
    val trip = Trip(pickUp, tripCompletionRules)
    tripsByBike.put(bike, trip)
    trip
  }

  def getCurrentTripForBike(bike: Bike): Option[Trip] = tripsByBike.get(bike)

  def finish(trip: Trip): TripResult = {
    val completedTrip = new FinishedTrip(trip.pickUp, now)
    val rulesCheckResult = tripCompletionRules.test(completedTrip)
    TripResult(completedTrip, rulesCheckResult)
  }

  def finishCurrentTripForBike(bike: Bike): Option[TripResult] = {
    getCurrentTripForBike(bike).map(finish)
  }
}