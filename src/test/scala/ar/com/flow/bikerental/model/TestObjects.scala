package ar.com.flow.bikerental.model

import java.time.LocalDateTime
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{InMemoryConsumedRentTokenRepository, InMemoryReservedRentTokenRepository, ReservedRentToken, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.TripCompletionRules

import scala.util.Random

trait TestObjects {
  val user: User = User(Some(1), "Emiliano Menéndez")

  val bike1: Bike = Bike("1")
  val bike2: Bike = Bike("2")

  val tokenRegistry: TokenRegistry =
    TokenRegistry(new Random,
                  new InMemoryReservedRentTokenRepository(),
                  new InMemoryConsumedRentTokenRepository())
  val tripCompletionRules: TripCompletionRules = TripCompletionRules.create
  val tripRegistry: TripRegistry = TripRegistry(tokenRegistry, tripCompletionRules)

  var reservedToken = ReservedRentToken("1", now.plusDays(1),  user)
  val expiredToken = ReservedRentToken(expiration = LocalDateTime.now.minusDays(1), owner = user)
}
