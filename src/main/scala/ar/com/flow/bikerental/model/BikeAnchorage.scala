package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import ar.com.flow.bikerental.model.trip.completion.TripResult

class BikeAnchorage(station: BikeStation, val trips: TripRegistry, bikeShop: BikeShop) {
  var parkedBike: Option[Bike] = None

  def isLocked = parkedBike.isDefined

  /**
   * @throws IllegalStateException if there is a previously parked Bike.
   * @return a { @link CompletedTrip} wrapped in an { @link Optional}
   */
  def parkBike(bike: Bike): Option[TripResult] = {
    require(parkedBike.isEmpty, "There is already a parked bike here.")
    parkedBike = Some(bike)
    trips.finishCurrentTripForBike(bike)
  }

  def releaseBike(token: ReservedRentToken): Option[Bike] = {
    require(!token.owner.isBanned, "The user is banned.")
    releaseParkedBikeIf(parked => !bikeShop.maintenanceRequests.exists(_ == parked))
      .map(bike => trips.startTrip(bike, token))
      .map(_.bike)
  }

  def releaseBike(token: BikeMaintenanceToken): Option[Bike] = {
    releaseParkedBikeIf(parked => parked == token.bike)
  }

  def requestBikeMaintenance(): Unit =
    parkedBike.map(bike => bikeShop.requestMaintenance(bike, station))

  private def releaseParkedBikeIf(filter: Bike => Boolean = _ => true): Option[Bike] = {
    val releasedBike = parkedBike.filter(filter)
    if (releasedBike.isDefined) {
      parkedBike = None
    }
    releasedBike
  }
}
