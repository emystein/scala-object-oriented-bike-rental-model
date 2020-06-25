package ar.com.flow.bikerental.model

trait BikeStationRepository {
  def save(bikeStation: BikeStation): Unit
  def getById(stationId: String): Option[BikeStation]
}
