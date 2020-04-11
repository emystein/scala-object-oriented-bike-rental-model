package ar.com.flow.bikerental.model

import java.time.LocalDateTime

case class BikeDropOffEvent(user: User, bike: Bike, timestamp: LocalDateTime) extends BikeEvent
