package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import ar.com.flow.bikerental.model.trip.completion.TripResult

case class BikeStation(id: Option[String], numberOfBikeAnchorages: Int, trips: TripRegistry, bikeShop: BikeShop) {
  val bikeAnchorages: IndexedSeq[BikeAnchorage]  = (1 to numberOfBikeAnchorages).map(i => new BikeAnchorage(trips, bikeShop))

  def getFreeSpots: Seq[BikeAnchorage] = bikeAnchorages.filter(anchorage => anchorage.parkedBike.isEmpty)

  def pickupAvailableBike(reservedToken: ReservedRentToken): Option[Bike] = getAnyBikeAnchorageWithParkedBike.flatMap((anchorage: BikeAnchorage) => anchorage.releaseBike(reservedToken))

  def getAnyBikeAnchorageWithParkedBike: Option[BikeAnchorage] = bikeAnchorages.find(anchorage => anchorage.parkedBike.isDefined)

  def getAnchorageById(anchorageId: Int): Option[BikeAnchorage] = {
    require(anchorageId > 0)

    bikeAnchorages.drop(anchorageId - 1).headOption
  }

  def getParkedBikes: Seq[BikeAnchorage] = bikeAnchorages.filter(_.parkedBike.isDefined)

  def parkBikeAtAnchorage(bike: Bike, anchorageId: Int) = {
    getAnchorageById(anchorageId).map(_.parkBike(bike))
  }
}