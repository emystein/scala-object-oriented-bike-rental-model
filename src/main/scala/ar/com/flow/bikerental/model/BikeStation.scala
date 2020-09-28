package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import ar.com.flow.bikerental.model.trip.completion.TripResult

case class BikeStation(id: Option[String], anchorageCount: Int, trips: TripRegistry, bikeShop: BikeShop) {
  val anchorages: IndexedSeq[BikeAnchorage] =
    (1 to anchorageCount).map(i => new BikeAnchorage(trips, bikeShop))

  def availableAnchorages: Seq[BikeAnchorage] = anchorages.filter(_.parkedBike.isEmpty)

  def occupiedAnchorages: Seq[BikeAnchorage] = anchorages diff availableAnchorages

  def pickupAvailableBike(rentToken: ReservedRentToken): Option[Bike] =
    occupiedAnchorages.headOption.flatMap(_.releaseBike(rentToken))

  def parkBikeOnAnchorage(bikeToPark: Bike, anchoragePosition: Int): Option[TripResult] =
    anchorageAt(anchoragePosition).flatMap(_.parkBike(bikeToPark))

  def anchorageAt(anchoragePosition: Int): Option[BikeAnchorage] =
    anchorages.drop(anchoragePosition - 1).headOption
}