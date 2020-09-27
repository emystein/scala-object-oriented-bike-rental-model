package ar.com.flow.bikerental.model

import java.time.{LocalDateTime, Period}

import ar.com.flow.bikerental.model.token.ReservedRentToken
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BikeAnchorageTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
    station = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop)
    anchorage = station.freeSpots.iterator.next
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

    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(expiredToken)
    }
  }

  test("givenABannedUserAndAParkedBikeWhenRetrieveTheBikeUsingAReservedTokenThenTheBikeAnchorageShouldRejectToReleaseTheBike") {
    anchorage.parkBike(bike1)
    reservedRentToken1.owner.ban(Period.ofDays(2))

    assertThrows[IllegalArgumentException] {
      anchorage.releaseBike(reservedRentToken1)
    }
  }
}