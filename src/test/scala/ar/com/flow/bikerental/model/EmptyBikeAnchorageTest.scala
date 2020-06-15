package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedToken, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.SuccessResult
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class EmptyBikeAnchorageTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var tokenRegistry: TokenRegistry = null
  private var bikeShop: BikeShop = null
  private var trips: TripRegistry = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
    trips = new TripRegistry(tripCompletionRules)
    station = new BikeStation(1, trips, bikeShop)
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

    val completedTrip = completedBikeTrip.get.completedTrip
    completedTrip.user shouldBe(trip.get.pickUp.reservedToken.owner)
    completedTrip.bike shouldBe(trip.get.pickUp.bike)
  }

  test("givenAnEmptyBikeAnchorageWhenParkABikeThenPostActionShouldBeExecuted") {
    anchorage.parkBike(bike1)
    anchorage.releaseBike(reservedRentToken1)
    val tripCompletionResult = anchorage.parkBike(bike1)

    tripCompletionResult shouldNot be(null)
    tripCompletionResult.get.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
  }

  test("givenAnEmptyBikeAnchorageWhenRetrieveTheBikeThenItShouldThrowIllegalArgumentException") {
    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(reservedRentToken1)
    }
  }
}