package ar.com.flow.bikerental.model

import scala.collection.mutable

class InMemoryBikeRepository extends BikeRepository {
  val bikesBySerialNumber: mutable.Map[String, Bike] = new mutable.HashMap[String, Bike]()

  override def save(bike: Bike): Unit = bikesBySerialNumber.put(bike.serialNumber, bike)

  override def getBySerialNumber(serialNumber: String): Option[Bike] = bikesBySerialNumber.get(serialNumber)

  override def getAll(): Iterable[Bike] = bikesBySerialNumber.values

  override def delete(bike: Bike): Unit = bikesBySerialNumber.remove(bike.serialNumber)
}
