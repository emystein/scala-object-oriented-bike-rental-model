package ar.com.flow.bikerental.model

import java.util.UUID

import scala.collection.mutable

class InMemoryBikeStationRepository extends BikeStationRepository {
  val stationsById: mutable.Map[String, BikeStation] = new mutable.HashMap()

  override def save(bikeStation: BikeStation): BikeStation ={
    val saveBikeStation = bikeStation.copy(id = Some(bikeStation.id.getOrElse(UUID.randomUUID().toString)))

    stationsById.put(saveBikeStation.id.get, saveBikeStation)

    saveBikeStation
  }

  override def getById(stationId: String): Option[BikeStation] = stationsById.get(stationId)
}
