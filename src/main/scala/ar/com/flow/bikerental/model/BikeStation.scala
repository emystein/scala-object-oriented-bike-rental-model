package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import ar.com.flow.bikerental.model.trip.completion.TripResult
import java.util.UUID

import scala.collection.mutable

// a BikeStation not persisted yet has no ID
case class BikeStation(id: Option[String], anchorageCount: Int, trips: TripRegistry, bikeShop: BikeShop, gps: BikeGps) {
  val anchorages: IndexedSeq[BikeAnchorage] =
    (1 to anchorageCount).map(i => new BikeAnchorage(this, trips, bikeShop))

  def availableAnchorages: Seq[BikeAnchorage] = anchorages.filter(_.parkedBike.isEmpty)

  def occupiedAnchorages: Seq[BikeAnchorage] = anchorages diff availableAnchorages

  def pickupAvailableBike(rentToken: ReservedRentToken): Option[Bike] =
    occupiedAnchorages.headOption.flatMap(_.releaseBike(rentToken))

  def parkBikeOnAnchorage(bikeToPark: Bike, anchoragePosition: Int): Option[TripResult] =
    anchorageAt(anchoragePosition).flatMap(_.parkBike(bikeToPark))

  def anchorageAt(position: Int): Option[BikeAnchorage] =
    anchorages.drop(position - 1).headOption

  def isAnchorageAvailable(position: Int): Boolean = anchorageAt(position).get.isAvailable
}

trait BikeStationRepository {
  def save(bikeStation: BikeStation): BikeStation

  def getById(stationId: String): Option[BikeStation]
}

class InMemoryBikeStationRepository extends BikeStationRepository {
  val stationsById: mutable.Map[String, BikeStation] = new mutable.HashMap()

  override def save(bikeStation: BikeStation): BikeStation = {
    val saveBikeStation = bikeStation.copy(id = Some(bikeStation.id.getOrElse(UUID.randomUUID().toString)), gps = bikeStation.gps)

    stationsById.put(saveBikeStation.id.get, saveBikeStation)

    saveBikeStation
  }

  override def getById(stationId: String): Option[BikeStation] = stationsById.get(stationId)
}
