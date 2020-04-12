package ar.com.flow.bikerental.model.trip.completion

import java.time.DayOfWeek
import java.util.function.Predicate

import ar.com.flow.bikerental.model.FinishedTrip
import ar.com.flow.bikerental.model.ban.BanUser

import scala.collection.mutable

case class TripCompletionRules(banRulesByDayOfWeek: mutable.Map[DayOfWeek, Predicate[FinishedTrip]]) {
  def test(trip: FinishedTrip): RulesCheckResult = {
    val rule = banRulesByDayOfWeek(trip.endDayOfWeek)
    val ruleAction = if (rule.test(trip)) BanUser(trip.user) else Pass()
    ruleAction.execute
  }
}