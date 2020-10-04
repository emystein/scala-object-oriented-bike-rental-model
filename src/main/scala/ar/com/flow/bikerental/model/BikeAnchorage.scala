package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import ar.com.flow.bikerental.model.trip.completion.TripResult

class BikeAnchorage(val trips: TripRegistry, bikeShop: BikeShop = new BikeShop()) {
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
    releaseParkedBikeIf(parked => !bikeShop.maintenanceRequests.exists(_.bike == parked))
      .map(bike => trips.startTrip(bike, token))
      .map(_.bike)
  }

  def releaseBike(token: BikeMaintenanceToken): Option[Bike] = {
    releaseParkedBikeIf(parked => parked == token.bike)
  }

  def requestBikeMaintenance(): Option[BikeMaintenanceRequest] =
    parkedBike.map(bike => bikeShop.requestMaintenance(bike))

  private def releaseParkedBikeIf(filter: Bike => Boolean = _ => true): Option[Bike] = {
    val releasedBike = parkedBike.filter(filter)
    if (releasedBike.isDefined) {
      parkedBike = None
    }
    releasedBike
  }
}
