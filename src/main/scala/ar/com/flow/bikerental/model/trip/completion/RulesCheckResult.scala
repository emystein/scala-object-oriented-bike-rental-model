package ar.com.flow.bikerental.model.trip.completion

trait RulesCheckResult {
  def userIsBanned: Boolean
}