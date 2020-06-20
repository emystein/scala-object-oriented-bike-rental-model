package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{InMemoryConsumedRentTokenRepository, InMemoryRentTokenRepository, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripCompletionRulesFactory}

import scala.util.Random

trait TestObjects {
  val user: User = User("1", "Emiliano Menéndez")
  val bike1: Bike = Bike("1")
  val bike2: Bike = Bike("2")
  val tripCompletionRules: TripCompletionRules = TripCompletionRulesFactory.create
  val tokenRegistry: TokenRegistry =
    TokenRegistry(new TokenGenerator(new Random),
                  new InMemoryRentTokenRepository(),
                  new InMemoryConsumedRentTokenRepository())
}
