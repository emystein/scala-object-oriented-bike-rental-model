package ar.com.flow.bikerental.model.ban

import java.time.Period

import ar.com.flow.bikerental.model.User
import ar.com.flow.bikerental.model.trip.completion.{AfterTripAction, UserBannedResult}

case class BanUser(user: User, defaultBanPeriod: Period = Period.ofDays(2) ) extends AfterTripAction {
  override def execute: UserBannedResult = {
    user.ban(defaultBanPeriod)
    new UserBannedResult
  }
}