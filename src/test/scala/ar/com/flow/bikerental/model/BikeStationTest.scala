package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedToken, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.TripCompletionRulesFactory
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class BikeStationTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private val tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
  private var trips: Trips = null
  private var reservedToken: ReservedToken = null

  override protected def beforeEach(): Unit = {
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    trips = new Trips(tripCompletionRules)
  }
  
  test("givenABikeStationWhenAskForAnchoragesThenItShouldReturnAnchorages") {
    val bikeStation = new BikeStation(2, trips)
   bikeStation.bikeAnchorages should have size(2)
  }

  test("givenNoParkedBikesWhenAskTheStationForParkedBikesThenItShouldRetrieveEmpty") {
    val bikeStation = new BikeStation(2, trips)
    bikeStation.getParkedBikes should be(Nil)
  }

  test("givenParkedBikesWhenAskTheStationForParkedBikesThenItShouldRetrieveTheBikes") {
    val bikeStation = new BikeStation(2, trips)
    fillStationWithBikes(bikeStation)
    bikeStation.getParkedBikes should have size(2)
  }

  test("givenNoParkedBikesWhenAskTheStationForFreeSpotsThenItShouldRetrieveAllAnchorages") {
    val bikeStation = new BikeStation(2, trips)
    bikeStation.getFreeSpots should have size(2)
  }

  test("givenAFullStationWhenAskTheStationForFreeSpotsThenItShouldRetrieveTheAnchorages") {
    val bikeStation = new BikeStation(2, trips)
    fillStationWithBikes(bikeStation)
    bikeStation.getFreeSpots should be(Nil)
  }

  test("givenAnAvailableBikeAndAValidTokenWhenPickUpABikeFromTheStationThenTheAvailableBikesShouldDecrement") {
    val bikeStation = new BikeStation(2, trips)
    fillStationWithBikes(bikeStation)
    val rentToken = tokenRegistry.reserveTokenForUser(user)
    bikeStation.pickupAvailableBike(rentToken)
    bikeStation.getParkedBikes should have size(1)
  }

  test("givenAnAvailableBikeAndAValidTokenWhenPickUpABikeFromTheStationThenItShouldReleaseTheBike") {
    val bikeStation = new BikeStation(1, trips)
    fillStationWithBikes(bikeStation)
    val bike = bikeStation.pickupAvailableBike(reservedToken)
    bike should be(defined)
  }

  test("givenNoAvailableBikesWhenPickupABikeFromTheStationThenItShouldNotReleaseAnyBike") {
    val bikeStation = new BikeStation(0, trips)
    val bike = bikeStation.pickupAvailableBike(reservedToken)
    bike shouldNot be(defined)
  }

  test("givenAUsedTokenWhenPickingUpABikeUsingItThenThePickupShouldBeRejected") {
    val bikeStation = new BikeStation(1, trips)
    fillStationWithBikes(bikeStation)
    // use valid rent token for the first time
    var bike = bikeStation.pickupAvailableBike(reservedToken)
    bike should be(defined)
    // try to use valid rent token for the second time
    bike = bikeStation.pickupAvailableBike(reservedToken)
    bike shouldNot be(defined)
  }

  private def fillStationWithBikes(bikeStation: BikeStation) = bikeStation.bikeAnchorages.foreach(_.parkBike(new Bike))
}