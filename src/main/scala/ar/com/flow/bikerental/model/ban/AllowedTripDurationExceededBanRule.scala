package ar.com.flow.bikerental.model.ban

import java.time.Duration
import java.util.function.Predicate

import ar.com.flow.bikerental.model.FinishedTrip

case class AllowedTripDurationExceededBanRule(maxAllowedTripDuration: Duration = null) extends Predicate[FinishedTrip] {
  override def test(trip: FinishedTrip): Boolean = trip.hasLastedMoreThan(maxAllowedTripDuration)
}