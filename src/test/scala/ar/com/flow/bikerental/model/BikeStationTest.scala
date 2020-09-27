package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BikeStationTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
  }
  
  test("givenABikeStationWhenAskForAnchoragesThenItShouldReturnAnchorages") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
   bikeStation.anchorages should have size 2
  }

  test("givenNoParkedBikesWhenAskTheStationForParkedBikesThenItShouldRetrieveEmpty") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
    bikeStation.occupiedSpots should be(Nil)
  }

  test("givenParkedBikesWhenAskTheStationForParkedBikesThenItShouldRetrieveTheBikes") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
    fillStationWithBikes(bikeStation)
    bikeStation.occupiedSpots should have size 2
  }

  test("givenNoParkedBikesWhenAskTheStationForFreeSpotsThenItShouldRetrieveAllAnchorages") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
    bikeStation.freeSpots should have size 2
  }

  test("givenAFullStationWhenAskTheStationForFreeSpotsThenItShouldRetrieveTheAnchorages") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
    fillStationWithBikes(bikeStation)
    bikeStation.freeSpots should be(Nil)
  }

  test("givenAnAvailableBikeAndAValidTokenWhenPickUpABikeFromTheStationThenTheAvailableBikesShouldDecrement") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
    fillStationWithBikes(bikeStation)
    val rentToken = tokenRegistry.reserveTokenForUser(user)
    bikeStation.pickupAvailableBike(rentToken)
    bikeStation.occupiedSpots should have size 1
  }

  test("givenAnAvailableBikeAndAValidTokenWhenPickUpABikeFromTheStationThenItShouldReleaseTheBike") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop)
    fillStationWithBikes(bikeStation)
    val bike = bikeStation.pickupAvailableBike(reservedToken)
    bike should be(defined)
  }

  test("givenNoAvailableBikesWhenPickupABikeFromTheStationThenItShouldNotReleaseAnyBike") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 0, tripRegistry, bikeShop)
    val bike = bikeStation.pickupAvailableBike(reservedToken)
    bike shouldNot be(defined)
  }

  test("givenAUsedTokenWhenPickingUpABikeUsingItThenThePickupShouldBeRejected") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop)
    fillStationWithBikes(bikeStation)
    // use valid rent token for the first time
    var bike = bikeStation.pickupAvailableBike(reservedToken)
    bike should be(defined)
    // try to use valid rent token for the second time
    bike = bikeStation.pickupAvailableBike(reservedToken)
    bike shouldNot be(defined)
  }

  test("givenAValidAnchorageWhenGetAnchorageByIdItShouldBePresent") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
    bikeStation.getAnchorageById(1) should be(defined)
    bikeStation.getAnchorageById(2) should be(defined)
  }

  test("givenAnInvalidAnchorageIdWhenGetAnchorageByIdItShouldReturnFirst") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
    bikeStation.getAnchorageById(-1) shouldBe defined
    bikeStation.getAnchorageById(0) shouldBe defined
  }

  test("givenAnAnchorageIdGreaterThanPresentWhenGetAnchorageByIdItShouldReturnNone") {
    val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)

    bikeStation.getAnchorageById(3) shouldNot be(defined)
  }

  private def fillStationWithBikes(bikeStation: BikeStation): Unit = bikeStation.anchorages.foreach(_.parkBike(new Bike))
}