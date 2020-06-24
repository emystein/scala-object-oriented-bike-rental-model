package ar.com.flow.bikerental.model

trait BikeStationRepository {
  def getById(stationId: String): Option[BikeStation]
}
