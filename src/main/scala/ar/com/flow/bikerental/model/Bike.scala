package ar.com.flow.bikerental.model

import java.util.UUID

case class Bike(serialNumber: String = UUID.randomUUID.toString) {
  var trips: Seq[Trip] = Seq.empty
  var currentTrip: Option[Trip] = None

  def addTrip(tripToAdd: Trip): Unit = {
    trips = tripToAdd +: trips
    currentTrip = Some(tripToAdd)
  }

  def finishTrip(): Unit = currentTrip = None
}

