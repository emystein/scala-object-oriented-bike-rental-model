package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken

case class BikeStation(id: Option[String], numberOfBikeAnchorages: Int, trips: TripRegistry, bikeShop: BikeShop) {
  val bikeAnchorages: IndexedSeq[BikeAnchorage] =
    (1 to numberOfBikeAnchorages).map(i => new BikeAnchorage(trips, bikeShop))

  def freeSpots: Seq[BikeAnchorage] = bikeAnchorages.filter(_.parkedBike.isEmpty)

  def occupiedSpots: Seq[BikeAnchorage] = bikeAnchorages diff freeSpots

  def pickupAvailableBike(rentToken: ReservedRentToken): Option[Bike] =
      occupiedSpots.headOption.flatMap(_.releaseBike(rentToken))

  def getAnchorageById(anchorageId: Int): Option[BikeAnchorage] =
    bikeAnchorages.drop(anchorageId - 1).headOption
}