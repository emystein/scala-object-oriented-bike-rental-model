package ar.com.flow.bikerental.model.trip.completion

import java.time.DayOfWeek
import java.util.function.Predicate

import ar.com.flow.bikerental.model.CompletedTrip
import ar.com.flow.bikerental.model.ban.BanUser

import scala.collection.mutable

case class TripCompletionRules(banRulesByDayOfWeek: mutable.Map[DayOfWeek, Predicate[CompletedTrip]]) {
  def test(trip: CompletedTrip): RulesCheckResult = {
    val rule = banRulesByDayOfWeek(trip.getEndDayOfWeek)
    val ruleAction = if (rule.test(trip)) BanUser(trip.user) else Pass()
    ruleAction.execute
  }
}