package ar.com.flow.bikerental.model

trait BikeAvailability {

}

case class Unavailable() extends BikeAvailability
case class NeedMaintenance() extends BikeAvailability
