package ar.com.flow.bikerental.model.trip.completion

import java.time.DayOfWeek
import java.time.Duration
import java.util.function.Predicate

import ar.com.flow.bikerental.model.DaysOfWeek.weekdays
import ar.com.flow.bikerental.model.DaysOfWeek.weekend
import java.time.Duration.ofHours

import ar.com.flow.bikerental.model.CompletedTrip
import ar.com.flow.bikerental.model.ban.AllowedTripDurationExceededBanRule

import scala.collection.mutable

object TripCompletionRulesFactory {
  def create: TripCompletionRules = TripCompletionRules(rulesByDayOfWeek)

  private def rulesByDayOfWeek: mutable.Map[DayOfWeek, Predicate[CompletedTrip]] = {
    val rules = new scala.collection.mutable.HashMap[DayOfWeek, Predicate[CompletedTrip]]
    weekdays.foreach(day => rules.put(day, allowMaxTripDuration(ofHours(1))))
    weekend.foreach(day => rules.put(day, allowMaxTripDuration(ofHours(2))))
    rules
  }

  private def allowMaxTripDuration(maxAllowedTripDuration: Duration): Predicate[CompletedTrip] = AllowedTripDurationExceededBanRule(maxAllowedTripDuration)
}