package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedRentToken, Token}

class BikeAnchorage(val trips: TripRegistry, bikeShop: BikeShop = new BikeShop()) {
  var parkedBike: Option[Bike] = None

  /**
   * @throws IllegalStateException if there is a previously parked Bike.
   * @return a { @link CompletedTrip} wrapped in an { @link Optional}
   */
  def parkBike(bike: Bike) = {
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
        releaseParkedBike.map(bike => trips.startTrip(bike, reservedToken)).map(_.pickUp.bike)
      case BikeMaintenanceToken(bike) =>
        parkedBike.filter(_ == bike)
    }
  }

  def requestBikeMaintenance(): Option[BikeMaintenanceRequest] = bikeShop.requestMaintenance(parkedBike)

  private def releaseParkedBike: Option[Bike] = {
    val releasedBike = parkedBike
    parkedBike = None
    releasedBike
  }
}
