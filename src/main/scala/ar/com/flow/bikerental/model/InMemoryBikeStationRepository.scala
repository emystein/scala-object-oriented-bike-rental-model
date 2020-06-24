package ar.com.flow.bikerental.model

import scala.collection.mutable

class InMemoryBikeStationRepository extends BikeStationRepository {
  val stationsById: mutable.Map[String, BikeStation] = new mutable.HashMap()

  override def getById(stationId: String): Option[BikeStation] = stationsById.get(stationId)
}
