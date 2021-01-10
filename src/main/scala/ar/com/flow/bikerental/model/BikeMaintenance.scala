package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.Token

import java.time.LocalDateTime

class BikeShop(gps: BikeGps) {
  var maintenanceRequests: List[Bike] = Nil

  var maintenanceRequestsProcessed: List[Bike] = Nil

  def requestMaintenance(bike: Bike, station: BikeStation): Unit = {
    maintenanceRequests = bike :: maintenanceRequests
    gps.informLocation(bike, station)
  }

  def hasBikeInMaintenance(bike: Bike): Boolean = maintenanceRequests.contains(bike)

  def nextMaintenancePickupToken() : Option[BikeMaintenanceToken] = {
    maintenanceRequests.headOption.map{ bike =>
      maintenanceRequestsProcessed = maintenanceRequestsProcessed :+ bike
      maintenanceRequests = maintenanceRequests.tail
      BikeMaintenanceToken(bike)
    }
  }
}

case class BikeMaintenanceToken(bike: Bike) extends Token {
  override var expiration: LocalDateTime = LocalDateTime.now().plusWeeks(1)
  override val owner: User = User(Some(0), "BikeShop")
}

