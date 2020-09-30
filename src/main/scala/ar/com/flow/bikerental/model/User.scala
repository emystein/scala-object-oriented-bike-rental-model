package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now
import java.time.{LocalDateTime, Period}

case class User(id: Option[Long] = None, name: String) {
  var ban: Option[UserBan] = None

  def isBanned = ban.isDefined

  def ban(banPeriod: Period): Unit = ban = Some(UserBan(this, now, banPeriod))
}

case class UserBan(bannedUser: User, beginningOfBan: LocalDateTime, banPeriod: Period)
