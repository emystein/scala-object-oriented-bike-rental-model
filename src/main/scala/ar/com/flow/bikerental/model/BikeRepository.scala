package ar.com.flow.bikerental.model

trait BikeRepository {
  def save(bike: Bike)
  def getBySerialNumber(serialNumber: String): Option[Bike]
  def getAll(): Iterable[Bike]
  def delete(bike: Bike)
  def clear(): Unit
}
