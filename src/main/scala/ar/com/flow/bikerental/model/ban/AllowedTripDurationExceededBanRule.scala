package ar.com.flow.bikerental.model.ban

import java.time.Duration
import java.util.function.Predicate

import ar.com.flow.bikerental.model.CompletedTrip

case class AllowedTripDurationExceededBanRule(maxAllowedTripDuration: Duration = null) extends Predicate[CompletedTrip] {
  override def test(trip: CompletedTrip): Boolean = trip.hasLastedMoreThan(maxAllowedTripDuration)
}