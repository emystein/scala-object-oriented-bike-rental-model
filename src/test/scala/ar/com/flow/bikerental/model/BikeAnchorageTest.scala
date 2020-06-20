package ar.com.flow.bikerental.model

import java.time.{LocalDateTime, Period}

import ar.com.flow.bikerental.model.token.{ReservedRentToken, RentToken, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.{SuccessResult, TripCompletionRules, TripCompletionRulesFactory}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class BikeAnchorageTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null
  private var trips: TripRegistry = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
    trips = new TripRegistry(tripCompletionRules)
    station = new BikeStation(1, trips, bikeShop)
    anchorage = station.getFreeSpots.iterator.next
  }

  test("givenABikeAnchorageWithAParkedBikeWhenAskingTheAnchorageForTheBikeThenItShouldReturnTheParkedBike") {
    anchorage.parkBike(bike1)

    anchorage.parkedBike shouldBe Some(bike1)
  }

  test("givenABikeAnchorageWithAParkedBikeWhenTryingToParkAnotherBikeThenAnErrorShouldOccur") {
    anchorage.parkBike(bike1)
    
    assertThrows[IllegalArgumentException] {
      anchorage.parkBike(bike2)
    }
  }

  test("givenABikeAnchorageWithAParkedBikeWhenRetrieveTheBikeThenTheAnchorageShouldBeEmpty") {
    anchorage.parkBike(bike1)
    anchorage.releaseBike(reservedRentToken1)

    anchorage.parkedBike shouldBe None
  }

  test("givenABikeAnchorageWithAParkedBikeWhenRetrieveTheBikeThenTheRetrieveBikeShouldBeTheParkedOne") {
    anchorage.parkBike(bike1)
    val retrievedBike = anchorage.releaseBike(reservedRentToken1)

    retrievedBike shouldBe Some(bike1)
  }

  test("givenABikeAnchorageWithAParkedBikeWhenRetrieveTheBikeUsingAnAlreadyUsedTokenThenTheAnchorageShouldNotReleaseTheBike") {
    anchorage.parkBike(bike1)
    anchorage.releaseBike(reservedRentToken1)
    anchorage.parkBike(bike1)

    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(reservedRentToken1)
    }
  }

  test("givenABikeAnchorageWithAParkedBikeWhenRetrieveTheBikeUsingAnExpiredTokenThenTheAnchorageShouldNotReleaseTheBike") {
    anchorage.parkBike(bike1)
    val expiredToken = new ReservedRentToken(new RentToken(owner = user, expiration = LocalDateTime.now.minusDays(1)), user, tokenRegistry)

    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(expiredToken)
    }
  }

  test("givenAParkedBikeWhenRetrieveTheBikeUsingAReservedTokenThenTheRetrieveBikeShouldBeAssociatedToTheToken") {
    anchorage.parkBike(bike1)
    val retrievedBike = anchorage.releaseBike(reservedRentToken1)
    val trip = trips.getCurrentTripForBike(retrievedBike.get)
    val bikePickUpEvent = trip.get.pickUp

    bikePickUpEvent.user shouldBe reservedRentToken1.owner
    bikePickUpEvent.consumedToken shouldNot be(null)
    bikePickUpEvent.bike shouldBe retrievedBike.get
  }

  test("givenABannedUserAndAParkedBikeWhenRetrieveTheBikeUsingAReservedTokenThenTheBikeAnchorageShouldRejectToReleaseTheBike") {
    anchorage.parkBike(bike1)
    reservedRentToken1.owner.ban(Period.ofDays(2))

    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(reservedRentToken1)
    }
  }
}