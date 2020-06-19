package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.Token

class BikeShop {
  def getMaintenancePickupToken(maintenanceRequest: Option[BikeMaintenanceRequest]): Option[BikeMaintenanceToken] = {
    maintenanceRequest.map{r => maintenanceRequestsProcessed = maintenanceRequestsProcessed :+ r; BikeMaintenanceToken(r.bike)}
  }

  var maintenancePickupRequests: List[BikeMaintenanceRequest] = Nil

  var maintenanceRequestsProcessed: List[BikeMaintenanceRequest] = Nil

  def requestMaintenance(maybeBike: Option[Bike]): Option[BikeMaintenanceRequest] = {
    maybeBike
      .map(bike => BikeMaintenanceRequest(bike))
      .map{pickup => maintenancePickupRequests = maintenancePickupRequests :+ pickup; pickup}
  }
}

case class BikeMaintenanceRequest(bike: Bike)

case class BikeMaintenanceToken(bike: Bike) extends Token

