package ar.com.flow.bikerental.model.trip.completion

import java.time.Duration.ofHours
import java.time.{DayOfWeek, Duration, Period}
import java.util.function.Predicate

import ar.com.flow.bikerental.model.DaysOfWeek.{weekdays, weekend}
import ar.com.flow.bikerental.model.FinishedTrip

object TripCompletionRules {
  val allowedDurationForWeekdays = allowMaxTripDuration(ofHours(1))
  val allowedDurationForWeekend = allowMaxTripDuration(ofHours(2))

  def create: TripCompletionRules = TripCompletionRules(rulesByDayOfWeek)

  private def rulesByDayOfWeek: Map[DayOfWeek, Predicate[FinishedTrip]] = {
    (weekdays.map(_ -> allowedDurationForWeekdays) ++ weekend.map(_ -> allowedDurationForWeekend)).toMap
  }

  private def allowMaxTripDuration(maxAllowedDuration: Duration): Predicate[FinishedTrip] = TripDurationExceededRule(maxAllowedDuration)
}

case class TripCompletionRules(banRulesByDayOfWeek: Map[DayOfWeek, Predicate[FinishedTrip]]) {
  def test(trip: FinishedTrip): RulesCheckResult = {
    val rule = banRulesByDayOfWeek(trip.endDayOfWeek)

    val ruleAction = if (rule.test(trip)) BanUserAfterTrip(trip.user, Period.ofDays(2)) else Pass()

    ruleAction.execute
  }
}

case class TripDurationExceededRule(maxAllowedTripDuration: Duration = null) extends Predicate[FinishedTrip] {
  override def test(trip: FinishedTrip): Boolean = trip.hasLastedMoreThan(maxAllowedTripDuration)
}
