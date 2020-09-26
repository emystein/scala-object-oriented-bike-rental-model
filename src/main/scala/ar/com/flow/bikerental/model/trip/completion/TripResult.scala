package ar.com.flow.bikerental.model.trip.completion

import ar.com.flow.bikerental.model.FinishedTrip

case class TripResult(completedTrip: FinishedTrip, rulesCheckResult: RulesCheckResult) {
  val isSuccess = rulesCheckResult.isInstanceOf[SuccessResult]
}
