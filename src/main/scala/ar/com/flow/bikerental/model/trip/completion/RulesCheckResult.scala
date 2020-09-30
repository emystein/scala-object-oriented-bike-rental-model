package ar.com.flow.bikerental.model.trip.completion

trait RulesCheckResult {
  def userIsBanned: Boolean
}

case class SuccessResult() extends RulesCheckResult {
  override def userIsBanned: Boolean = false
}

class UserBannedResult extends RulesCheckResult {
  override def userIsBanned: Boolean = true
}
