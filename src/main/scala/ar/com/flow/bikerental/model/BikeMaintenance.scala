package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.Token

class BikeShop {
  var maintenancePickupRequests: List[BikeMaintenanceRequest] = Nil

  var maintenanceRequestsProcessed: List[BikeMaintenanceRequest] = Nil

  def requestMaintenance(maybeBike: Option[Bike]): Option[BikeMaintenanceRequest] = {
    maybeBike
      .map(bike => BikeMaintenanceRequest(bike))
      .map{pickup => maintenancePickupRequests = maintenancePickupRequests :+ pickup; pickup}
  }

  def nextMaintenancePickupToken() : Option[BikeMaintenanceToken] = {
    maintenancePickupRequests.headOption.map{r => maintenanceRequestsProcessed = maintenanceRequestsProcessed :+ r; BikeMaintenanceToken(r.bike)}
  }
}

case class BikeMaintenanceRequest(bike: Bike)

case class BikeMaintenanceToken(bike: Bike) extends Token

