package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken

case class BikeStation(id: Option[String], numberOfBikeAnchorages: Int, trips: TripRegistry, bikeShop: BikeShop) {
  val bikeAnchorages: IndexedSeq[BikeAnchorage] =
    (1 to numberOfBikeAnchorages).map(i => new BikeAnchorage(trips, bikeShop))

  def getFreeSpots: Seq[BikeAnchorage] = bikeAnchorages.filter(_.parkedBike.isEmpty)

  def pickupAvailableBike(reservedToken: ReservedRentToken): Option[Bike] =
    getAnyBikeAnchorageWithParkedBike.flatMap(_.releaseBike(reservedToken))

  def getAnyBikeAnchorageWithParkedBike: Option[BikeAnchorage] =
    bikeAnchorages.find(_.parkedBike.isDefined)

  def getAnchorageById(anchorageId: Int): Option[BikeAnchorage] =
    bikeAnchorages.drop(anchorageId - 1).headOption

  def getParkedBikes: Seq[BikeAnchorage] =
    bikeAnchorages.filter(_.parkedBike.isDefined)
}