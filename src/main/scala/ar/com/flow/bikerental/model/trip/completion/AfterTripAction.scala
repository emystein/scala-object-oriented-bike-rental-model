package ar.com.flow.bikerental.model.trip.completion

import java.time.Period

import ar.com.flow.bikerental.model.User

trait AfterTripAction {
  def execute: RulesCheckResult
}

case class Pass() extends AfterTripAction {
  override def execute = new SuccessResult
}

case class BanUserAfterTrip(userToBan: User, banPeriod: Period) extends AfterTripAction {
  override def execute: RulesCheckResult = {
    userToBan.ban(banPeriod)

    new UserBannedResult
  }
}

