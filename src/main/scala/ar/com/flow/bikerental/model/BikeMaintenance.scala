package ar.com.flow.bikerental.model

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.token.Token

class BikeShop {
  var maintenanceRequests: List[Bike] = Nil

  var maintenanceRequestsProcessed: List[Bike] = Nil

  def requestMaintenance(bike: Bike): Unit = {
    maintenanceRequests = bike :: maintenanceRequests
  }

  def hasBikeInMaintenance(bike: Bike) = maintenanceRequests.contains(bike)

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

