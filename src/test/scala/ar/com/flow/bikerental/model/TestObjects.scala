package ar.com.flow.bikerental.model

import java.time.LocalDateTime
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{InMemoryConsumedRentTokenRepository, InMemoryReservedRentTokenRepository, ReservedRentToken, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripCompletionRulesFactory}

import scala.util.Random

trait TestObjects {
  val user: User = User("1", "Emiliano Menéndez")
  val bike1: Bike = Bike("1")
  val bike2: Bike = Bike("2")
  val tripCompletionRules: TripCompletionRules = TripCompletionRulesFactory.create
  val tokenRegistry: TokenRegistry =
    TokenRegistry(new Random,
                  new InMemoryReservedRentTokenRepository(),
                  new InMemoryConsumedRentTokenRepository())
  var reservedToken = ReservedRentToken("1", now.plusDays(1),  user, tokenRegistry)
  val expiredToken = ReservedRentToken(expiration = LocalDateTime.now.minusDays(1), owner = user, tokenRegistry = tokenRegistry)
}
