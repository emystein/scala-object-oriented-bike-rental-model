package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{InMemoryConsumedRentTokenRepository, InMemoryReservedRentTokenRepository, ReservedRentToken, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.TripCompletionRules
import org.scalatest.{BeforeAndAfterEach, Suite}

import java.time.LocalDateTime
import java.time.LocalDateTime.now
import scala.util.Random

trait TestObjects extends Suite with BeforeAndAfterEach {
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

  var gps: BikeGps = BikeGps()

  var bikeShop: BikeShop = new BikeShop(gps)

  var bikeStation: BikeStation with BikeStationTestMethods = new BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop, gps)  with BikeStationTestMethods

  var anchorage: BikeAnchorage = bikeStation.availableAnchorages.iterator.next

  var reservedRentToken1: ReservedRentToken = tokenRegistry.reserveTokenForUser(user)

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    gps = BikeGps()
    bikeShop = new BikeShop(gps)
    bikeStation = new BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop, gps) with BikeStationTestMethods
    anchorage = bikeStation.availableAnchorages.iterator.next
  }
}
