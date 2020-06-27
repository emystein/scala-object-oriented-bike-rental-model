package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now
import java.time.Period

import ar.com.flow.bikerental.model
import ar.com.flow.bikerental.model.ban.UserBan

case class User(id: Option[String], name: String) {
  var ban: Option[UserBan] = None

  def isBanned = ban.isDefined

  def ban(period: Period): Unit = ban = Some(model.ban.UserBan(this, now, period))
}