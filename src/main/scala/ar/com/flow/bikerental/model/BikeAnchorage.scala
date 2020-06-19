package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedRentToken, Token}
import ar.com.flow.bikerental.model.trip.completion.TripResult

class BikeAnchorage(val trips: TripRegistry, bikeShop: BikeShop = new BikeShop()) {
  var parkedBike: Option[Bike] = None

  /**
   * @throws IllegalStateException if there is a previously parked Bike.
   * @return a { @link CompletedTrip} wrapped in an { @link Optional}
   */
  def parkBike(bike: Bike): Option[TripResult] = {
    require(parkedBike.isEmpty, "There is already a parked bike here.")
    parkedBike = Some(bike)
    trips.finishCurrentTripForBike(bike)
  }

  def isLocked = parkedBike.isDefined

  /**
   * Given a {@link Token}, consumes the token and releases the {@link Bike}.
   *
   * @param token the RentToken.
   * @return the Bike if present and reservedToken is OK, None otherwise.
   * @throws IllegalStateException if the owner of the RentToken is banned
   */
  def releaseBike(token: Token): Option[Bike] = {
    token match {
      case reservedToken@ReservedRentToken(_, owner, _) =>
        require(!owner.isBanned, "The user is banned.")
        releaseParkedBike(parked => !bikeShop.maintenancePickupRequests.exists(r => r.bike == parked))
          .map(bike => trips.startTrip(bike, reservedToken))
          .map(_.pickUp.bike)
      case BikeMaintenanceToken(bike) =>
        releaseParkedBike(parked => parked == bike)
    }
  }

  def requestBikeMaintenance(): Option[BikeMaintenanceRequest] = bikeShop.requestMaintenance(parkedBike)

  private def releaseParkedBike(filter: Bike => Boolean = _ => true): Option[Bike] = {
    val releasedBike = parkedBike.filter(filter)
    if (releasedBike.isDefined) {
      parkedBike = None
    }
    releasedBike
  }
}
