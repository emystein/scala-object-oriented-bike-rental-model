package ar.com.flow.bikerental.model

class BikeShop {
  def getMaintenancePickupToken(maintenanceRequest: BikeMaintenanceRequest): Option[BikeMaintenancePickupToken] = {
    getMaintenancePickupToken(Some(maintenanceRequest))
  }

  def getMaintenancePickupToken(maintenanceRequest: Option[BikeMaintenanceRequest]): Option[BikeMaintenancePickupToken] = {
    maintenanceRequest.map{r => maintenanceRequestsProcessed = maintenanceRequestsProcessed :+ r; BikeMaintenancePickupToken(r.bike)}
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

case class BikeMaintenancePickupToken(bike: Bike)
