package ar.com.flow.bikerental.model

import scala.collection.mutable

object BikeLocationRegistry {
  var relativeLocation: mutable.Map[Bike, RelativeBikeLocation] = mutable.Map()

  def setInTransitToShop(bike: Bike): Unit = {
    relativeLocation(bike) = InTransitToShop()
  }

  def relativeLocationOf(bike: Bike) = relativeLocation(bike)
}

sealed trait RelativeBikeLocation

case class InTransitToShop() extends RelativeBikeLocation
