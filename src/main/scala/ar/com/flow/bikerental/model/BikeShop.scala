package ar.com.flow.bikerental.model

class BikeShop {
  var maintenancePickupRequests: List[BikeMaintenancePickup] = Nil

  def requestMaintenancePickup(maybeBike: Option[Bike]) = {
    maybeBike.map(bike => maintenancePickupRequests = maintenancePickupRequests :+ BikeMaintenancePickup(bike))
  }
}

case class BikeMaintenancePickup(bike: Bike)

