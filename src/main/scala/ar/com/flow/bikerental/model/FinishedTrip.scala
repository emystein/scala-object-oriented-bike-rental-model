package ar.com.flow.bikerental.model

import java.time.{DayOfWeek, Duration, LocalDateTime}

class FinishedTrip(var bikePickup: BikePickUpEvent, val dropOffTimestamp: LocalDateTime) {
  val user = bikePickup.user
  val bike = bikePickup.bike
  val bikeDropOff = BikeDropOffEvent(bikePickup.user, bikePickup.bike, dropOffTimestamp)

  def getDuration: Duration = Duration.between(bikePickup.timestamp, bikeDropOff.timestamp)

  def getEndDayOfWeek: DayOfWeek = bikeDropOff.timestamp.getDayOfWeek

  def hasLastedMoreThan(duration: Duration): Boolean = getDuration.compareTo(duration) > 0
}