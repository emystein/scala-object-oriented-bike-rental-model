package ar.com.flow.bikerental.model

import java.util.UUID

case class Bike(serialNumber: String = UUID.randomUUID.toString) {
  var maintenanceStatus: Option[MaintenanceStatus] = None
}

