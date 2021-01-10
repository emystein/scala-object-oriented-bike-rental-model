package ar.com.flow.bikerental.model

import scala.collection.mutable

case class BikeGps() {
  var bikeLocations: mutable.Map[Bike, BikeStation] = mutable.Map.empty[Bike, BikeStation]

  def informLocation(bike: Bike, station: BikeStation): Unit = bikeLocations.put(bike, station)

  def bikeLocation(bike: Bike): Option[BikeStation] = bikeLocations.get(bike)
}
