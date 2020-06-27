package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BikeStationTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null
  private var trips: TripRegistry = null

  override protected def beforeEach(): Unit = {
    tokenRegistry.deleteAll()
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
    trips = TripRegistry(tripCompletionRules)
  }
  
  test("givenABikeStationWhenAskForAnchoragesThenItShouldReturnAnchorages") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)
   bikeStation.bikeAnchorages should have size 2
  }

  test("givenNoParkedBikesWhenAskTheStationForParkedBikesThenItShouldRetrieveEmpty") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)
    bikeStation.getParkedBikes should be(Nil)
  }

  test("givenParkedBikesWhenAskTheStationForParkedBikesThenItShouldRetrieveTheBikes") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)
    fillStationWithBikes(bikeStation)
    bikeStation.getParkedBikes should have size 2
  }

  test("givenNoParkedBikesWhenAskTheStationForFreeSpotsThenItShouldRetrieveAllAnchorages") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)
    bikeStation.getFreeSpots should have size 2
  }

  test("givenAFullStationWhenAskTheStationForFreeSpotsThenItShouldRetrieveTheAnchorages") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)
    fillStationWithBikes(bikeStation)
    bikeStation.getFreeSpots should be(Nil)
  }

  test("givenAnAvailableBikeAndAValidTokenWhenPickUpABikeFromTheStationThenTheAvailableBikesShouldDecrement") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)
    fillStationWithBikes(bikeStation)
    val rentToken = tokenRegistry.reserveTokenForUser(user)
    bikeStation.pickupAvailableBike(rentToken)
    bikeStation.getParkedBikes should have size 1
  }

  test("givenAnAvailableBikeAndAValidTokenWhenPickUpABikeFromTheStationThenItShouldReleaseTheBike") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 1, trips, bikeShop)
    fillStationWithBikes(bikeStation)
    val bike = bikeStation.pickupAvailableBike(reservedToken)
    bike should be(defined)
  }

  test("givenNoAvailableBikesWhenPickupABikeFromTheStationThenItShouldNotReleaseAnyBike") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 0, trips, bikeShop)
    val bike = bikeStation.pickupAvailableBike(reservedToken)
    bike shouldNot be(defined)
  }

  test("givenAUsedTokenWhenPickingUpABikeUsingItThenThePickupShouldBeRejected") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 1, trips, bikeShop)
    fillStationWithBikes(bikeStation)
    // use valid rent token for the first time
    var bike = bikeStation.pickupAvailableBike(reservedToken)
    bike should be(defined)
    // try to use valid rent token for the second time
    bike = bikeStation.pickupAvailableBike(reservedToken)
    bike shouldNot be(defined)
  }

  test("givenAValidAnchorageWhenGetAnchorageByIdItShouldBePresent") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)
    bikeStation.getAnchorageById(1) should be(defined)
    bikeStation.getAnchorageById(2) should be(defined)
  }

  test("givenAnInvalidAnchorageIdWhenGetAnchorageByIdItShouldThrowAnException") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)

    assertThrows[IllegalArgumentException](
      bikeStation.getAnchorageById(0)
    )
  }

  test("givenAnAnchorageIdGreaterThanPresentWhenGetAnchorageByIdItShouldReturnNone") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)

    bikeStation.getAnchorageById(3) shouldNot be(defined)
  }

  test("givenABikeWhenParkBikeAtAnchorageItShouldAcceptTheBike") {
    val bikeStation = BikeStation(Some("1"), numberOfBikeAnchorages = 2, trips, bikeShop)

    bikeStation.parkBikeAtAnchorage(bike1, 1)

    bikeStation.getAnchorageById(1).get.parkedBike shouldBe Some(bike1)
  }

  private def fillStationWithBikes(bikeStation: BikeStation): Unit = bikeStation.bikeAnchorages.foreach(_.parkBike(new Bike))
}