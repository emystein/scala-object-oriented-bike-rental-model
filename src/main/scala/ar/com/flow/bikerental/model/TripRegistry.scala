package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripResult}

import scala.collection.mutable

case class TripRegistry(tokenRegistry: TokenRegistry, tripCompletionRules: TripCompletionRules) {
  private val tripsByBike = new mutable.HashMap[Bike, Trip]

  def startTrip(bike: Bike, reservedToken: ReservedRentToken): Trip = {
    val consumedToken = tokenRegistry.consumeToken(reservedToken)
    val trip = Trip(bike, consumedToken)
    tripsByBike.put(bike, trip)
    trip
  }

  def getCurrentTripForBike(bike: Bike): Option[Trip] = bike.currentTrip

  def finishTrip(trip: Trip): TripResult = {
    trip.finish(tripCompletionRules)
  }

  def finishCurrentTripForBike(bike: Bike): Option[TripResult] = {
    bike.currentTrip.map(finishTrip)
  }

  def clear(): Unit = tripsByBike.clear()
}