package ar.com.flow.bikerental.model.trip.completion

case class SuccessResult() extends RulesCheckResult {
  override def userIsBanned: Boolean = false
}