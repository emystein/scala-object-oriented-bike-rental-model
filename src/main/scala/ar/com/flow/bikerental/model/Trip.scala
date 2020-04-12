package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripResult}

case class Trip(pickUp: BikePickUpEvent, completionRules: TripCompletionRules) {
  def finish: TripResult = {
    val completedTrip = new CompletedTrip(pickUp, now)
    val rulesCheckResult = completionRules.test(completedTrip)
    TripResult(completedTrip, rulesCheckResult)
  }
}