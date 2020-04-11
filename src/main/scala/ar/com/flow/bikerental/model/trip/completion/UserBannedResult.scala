package ar.com.flow.bikerental.model.trip.completion

class UserBannedResult extends RulesCheckResult {
  override def userIsBanned: Boolean = true
}