package ar.com.flow.bikerental.model

import java.time.{DayOfWeek, Duration, LocalDateTime}

class FinishedTrip(var bikePickup: BikePickUpEvent, val dropOffTimestamp: LocalDateTime) {
  val user = bikePickup.user
  val bike = bikePickup.bike
  val bikeDropOff = BikeDropOffEvent(bikePickup.user, bikePickup.bike, dropOffTimestamp)

  def duration: Duration = Duration.between(bikePickup.timestamp, bikeDropOff.timestamp)

  def endDayOfWeek: DayOfWeek = bikeDropOff.timestamp.getDayOfWeek

  def hasLastedMoreThan(duration: Duration): Boolean = this.duration.compareTo(duration) > 0
}