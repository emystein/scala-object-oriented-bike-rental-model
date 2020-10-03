package ar.com.flow.bikerental.model

import java.util.UUID
import scala.collection.mutable

case class Bike(serialNumber: String = UUID.randomUUID.toString) {
  var trips: Seq[Trip] = Seq.empty
  var currentTrip: Option[Trip] = None

  def addTrip(tripToAdd: Trip): Unit = {
    trips = tripToAdd +: trips
    currentTrip = Some(tripToAdd)
  }

  def finishTrip(): Unit = currentTrip = None
}

trait BikeRepository {
  def save(bike: Bike)
  def getBySerialNumber(serialNumber: String): Option[Bike]
  def getAll(): Iterable[Bike]
  def delete(bike: Bike)
  def clear(): Unit
}

class InMemoryBikeRepository extends BikeRepository {
  val bikesBySerialNumber: mutable.Map[String, Bike] = new mutable.HashMap[String, Bike]()

  override def save(bike: Bike): Unit = bikesBySerialNumber.put(bike.serialNumber, bike)

  override def getBySerialNumber(serialNumber: String): Option[Bike] = bikesBySerialNumber.get(serialNumber)

  override def getAll(): Iterable[Bike] = bikesBySerialNumber.values

  override def delete(bike: Bike): Unit = bikesBySerialNumber.remove(bike.serialNumber)

  override def clear(): Unit = bikesBySerialNumber.clear()
}
