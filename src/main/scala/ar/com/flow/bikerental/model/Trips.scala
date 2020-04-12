package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedToken
import ar.com.flow.bikerental.model.trip.completion.TripCompletionRules

import scala.collection.mutable

case class Trips(tripCompletionRules: TripCompletionRules) {
  private val tripsByBike = new mutable.HashMap[Bike, Trip]

  def startTrip(bike: Bike, reservedToken: ReservedToken): Trip = {
    val trip = Trip(bike, reservedToken, tripCompletionRules)
    tripsByBike.put(bike, trip)
    trip
  }

  def getCurrentTripForBike(bike: Bike): Option[Trip] = tripsByBike.get(bike)
}