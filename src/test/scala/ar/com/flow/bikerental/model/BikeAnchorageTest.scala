package ar.com.flow.bikerental.model

import java.time.{LocalDateTime, Period}

import ar.com.flow.bikerental.model.token.{ReservedToken, Token, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.{SuccessResult, TripCompletionRules, TripCompletionRulesFactory}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class BikeAnchorageTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var tokenRegistry: TokenRegistry = null
  private var trips: Trips = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    trips = new Trips(tripCompletionRules)
    station = new BikeStation(1, trips)
    anchorage = station.getFreeSpots.iterator.next
  }

  test("givenAnEmptyBikeAnchorageWhenAskForBikeThenItShouldReturnEmpty") {
    anchorage.parkedBike shouldBe None
  }

  test("givenAnEmptyBikeAnchorageWhenAskIfItsLockedThenItShouldReturnFalse") {
    anchorage.isLocked shouldBe false
  }

  test("givenAnEmptyBikeAnchorageWhenParkABikeThenTheAnchorageShouldBeLocked") {
    anchorage.parkBike(bike1)

    anchorage.isLocked shouldBe true
  }

  test("givenAnEmptyBikeAnchorageWhenParkABikeThenACompletedBikeTripShouldBePresent") { // In order to release a bike, first the anchorage must have a parked bike.
    anchorage.parkBike(bike1)
    // Releasing a bike starts a trip
    anchorage.releaseBike(reservedRentToken1)
    val completedBikeTrip = anchorage.parkBike(bike1)
    val trip = trips.getCurrentTripForBike(bike1)

    completedBikeTrip.get.completedTrip.user shouldBe(trip.get.reservedToken.owner)
    completedBikeTrip.get.completedTrip.bike shouldBe(trip.get.bike)
  }

  test("givenAnEmptyBikeAnchorageWhenParkABikeThenPostActionShouldBeExecuted") {
    anchorage.parkBike(bike1)
    anchorage.releaseBike(reservedRentToken1)
    val tripCompletionResult = anchorage.parkBike(bike1)

    tripCompletionResult shouldNot be(null)
    tripCompletionResult.get.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
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

  test("givenAnEmptyBikeAnchorageWhenRetrieveTheBikeThenItShouldThrowIllegalArgumentException") {
    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(reservedRentToken1)
    }
  }

  test("givenABikeAnchorageWithAParkedBikeWhenRetrieveTheBikeThenTheRetrieveBikeShouldBeTheParkedOne") {
    anchorage.parkBike(bike1)
    val retrievedBike = anchorage.releaseBike(reservedRentToken1)

    retrievedBike shouldBe bike1
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
    val expiredToken = new ReservedToken(new Token(owner = user, expiration = LocalDateTime.now.minusDays(1)), user, tokenRegistry)

    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(expiredToken)
    }
  }

  test("givenAParkedBikeWhenRetrieveTheBikeUsingAReservedTokenThenTheRetrieveBikeShouldBeAssociatedToTheToken") {
    anchorage.parkBike(bike1)
    val retrievedBike = anchorage.releaseBike(reservedRentToken1)
    val trip = trips.getCurrentTripForBike(retrievedBike)
    val bikePickUpEvent = trip.get.pickUp

    bikePickUpEvent.user shouldBe reservedRentToken1.owner
    bikePickUpEvent.consumedToken shouldNot be(null)
    bikePickUpEvent.bike shouldBe retrievedBike
  }

  test("givenABannedUserAndAParkedBikeWhenRetrieveTheBikeUsingAReservedTokenThenTheBikeAnchorageShouldRejectToReleaseTheBike") {
    anchorage.parkBike(bike1)
    reservedRentToken1.owner.ban(Period.ofDays(2))

    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(reservedRentToken1)
    }
  }
}