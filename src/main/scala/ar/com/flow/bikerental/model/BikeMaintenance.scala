package ar.com.flow.bikerental.model

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.token.Token

class BikeShop {
  var maintenanceRequests: List[BikeMaintenanceRequest] = Nil

  var maintenanceRequestsProcessed: List[BikeMaintenanceRequest] = Nil

  def requestMaintenance(bike: Bike): BikeMaintenanceRequest = {
    maintenanceRequests = BikeMaintenanceRequest(bike) :: maintenanceRequests
    maintenanceRequests.head
  }

  def hasBikeInMaintenance(bike: Bike) = maintenanceRequests.contains(BikeMaintenanceRequest(bike))

  def nextMaintenancePickupToken() : Option[BikeMaintenanceToken] = {
    maintenanceRequests.headOption.map{ request =>
      maintenanceRequestsProcessed = maintenanceRequestsProcessed :+ request
      maintenanceRequests = maintenanceRequests.tail
      BikeMaintenanceToken(request.bike)
    }
  }
}

case class BikeMaintenanceRequest(bike: Bike)

case class BikeMaintenanceToken(bike: Bike) extends Token {
  override var expiration: LocalDateTime = LocalDateTime.now().plusWeeks(1)
  override val owner: User = User(Some(0), "BikeShop")
}

