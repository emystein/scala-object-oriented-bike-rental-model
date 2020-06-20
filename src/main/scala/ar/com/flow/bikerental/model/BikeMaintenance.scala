package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.Token

class BikeShop {
  var maintenancePickupRequests: List[BikeMaintenanceRequest] = Nil

  var maintenanceRequestsProcessed: List[BikeMaintenanceRequest] = Nil

  def requestMaintenance(bike: Bike): BikeMaintenanceRequest = {
    maintenancePickupRequests = BikeMaintenanceRequest(bike) :: maintenancePickupRequests
    maintenancePickupRequests.head
  }

  def nextMaintenancePickupToken() : Option[BikeMaintenanceToken] = {
    maintenancePickupRequests.headOption.map{ request =>
      maintenanceRequestsProcessed = maintenanceRequestsProcessed :+ request
      maintenancePickupRequests = maintenancePickupRequests.tail
      BikeMaintenanceToken(request.bike)
    }
  }
}

case class BikeMaintenanceRequest(bike: Bike)

case class BikeMaintenanceToken(bike: Bike) extends Token

