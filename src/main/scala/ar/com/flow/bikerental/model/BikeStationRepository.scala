package ar.com.flow.bikerental.model

trait BikeStationRepository {
  def save(bikeStation: BikeStation): BikeStation
  def getById(stationId: String): Option[BikeStation]
}
