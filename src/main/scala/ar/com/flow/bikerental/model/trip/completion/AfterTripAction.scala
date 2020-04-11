package ar.com.flow.bikerental.model.trip.completion

trait AfterTripAction {
  def execute: RulesCheckResult
}