package ar.com.flow.bikerental.model

class BikeShop {
  var maintenancePickupRequests: List[BikeMaintenancePickup] = Nil

  def requestMaintenancePickup(maybeBike: Option[Bike]): Option[BikeMaintenancePickup] = {
    maybeBike
      .map(bike => BikeMaintenancePickup(bike))
      .map{pickup => maintenancePickupRequests = maintenancePickupRequests :+ pickup; pickup}
  }
}

case class BikeMaintenancePickup(bike: Bike)

