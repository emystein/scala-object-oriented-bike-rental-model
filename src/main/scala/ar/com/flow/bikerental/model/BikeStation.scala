package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken

class BikeStation(val numberOfBikeAnchorages: Int, val trips: TripRegistry, val bikeShop: BikeShop) {
  val bikeAnchorages = (1 to numberOfBikeAnchorages).map(i => new BikeAnchorage(trips, bikeShop))

  def getFreeSpots = bikeAnchorages.filter(anchorage => anchorage.parkedBike.isEmpty)

  def pickupAvailableBike(reservedToken: ReservedRentToken) = getAnyBikeAnchorageWithParkedBike.map((anchorage: BikeAnchorage) => anchorage.releaseBike(reservedToken))

  def getAnyBikeAnchorageWithParkedBike = bikeAnchorages.find(anchorage => anchorage.parkedBike.isDefined)

  def getParkedBikes = bikeAnchorages.filter(_.parkedBike.isDefined)
}