package ar.com.flow.bikerental.model.ban

import java.time.{LocalDateTime, Period}

import ar.com.flow.bikerental.model.User

case class UserBan(user: User, startTime: LocalDateTime, period: Period)
