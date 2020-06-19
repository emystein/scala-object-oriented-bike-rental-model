package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedToken

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
   * @param reservedToken the RentToken.
   * @return the Bike if present and reservedToken is OK, None otherwise.
   * @throws IllegalStateException if the owner of the RentToken is banned
   */
  def releaseBike(reservedToken: ReservedToken): Option[Bike] = {
    require(!reservedToken.owner.isBanned, "The user is banned.")
    releaseParkedBike.map(bike => trips.startTrip(bike, reservedToken)).map(_.pickUp.bike)
  }

  def requestBikeMaintenance(): Option[BikeMaintenanceRequest] = bikeShop.requestMaintenance(parkedBike)

  def releaseBike(token: BikeMaintenanceToken): Option[Bike] = {
    parkedBike.filter(_ == token.bike)
  }

  private def releaseParkedBike: Option[Bike] = {
    val releasedBike = parkedBike
    parkedBike = None
    releasedBike
  }
}
