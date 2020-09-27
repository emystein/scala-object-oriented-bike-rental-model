package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken

case class BikeStation(id: Option[String], anchorageCount: Int, trips: TripRegistry, bikeShop: BikeShop) {
  val anchorages: IndexedSeq[BikeAnchorage] =
    (1 to anchorageCount).map(i => new BikeAnchorage(trips, bikeShop))

  def freeAnchorages: Seq[BikeAnchorage] = anchorages.filter(_.parkedBike.isEmpty)

  def occupiedAnchorages: Seq[BikeAnchorage] = anchorages diff freeAnchorages

  def pickupAvailableBike(rentToken: ReservedRentToken): Option[Bike] =
      occupiedAnchorages.headOption.flatMap(_.releaseBike(rentToken))

  def getAnchorageById(anchorageId: Int): Option[BikeAnchorage] =
    anchorages.drop(anchorageId - 1).headOption
}