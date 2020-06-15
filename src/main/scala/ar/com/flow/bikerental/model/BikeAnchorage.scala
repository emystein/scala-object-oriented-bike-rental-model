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
   * @return the Bike.
   * @throws IllegalStateException if there is no parked Bike, or the owner of the RentToken is banned
   */
  def releaseBike(reservedToken: ReservedToken) = {
    require(parkedBike.isDefined, "There's no parked bike.")
    require(!reservedToken.owner.isBanned, "The user is banned.")
    val releasedBike = releaseParkedBike
    trips.startTrip(releasedBike, reservedToken)
    releasedBike
  }

  def requestBikeMaintenance() = bikeShop.requestMaintenancePickup(parkedBike)

  def releaseForService(maintenancePickupRequest: BikeMaintenancePickup) = {
    val bike = maintenancePickupRequest.bike
    BikeLocationRegistry.setInTransitToShop(bike)
    bike
  }

  private def releaseParkedBike = {
    val releasedBike = parkedBike.get
    parkedBike = None
    releasedBike
  }
}
