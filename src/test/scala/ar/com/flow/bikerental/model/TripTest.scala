package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TripTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var pickUpEvent: BikePickUpEvent = null

  override protected def beforeEach(): Unit = {
    tokenRegistry.deleteAll()
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    pickUpEvent = BikePickUpEvent(bike1, reservedToken)
  }

  test("givenAnInitialStateWhenCreateTripThenPickUpShouldBePresent") {
    val trip = Trip(pickUpEvent)
    trip.pickUp shouldNot be(null)
  }

  test("givenACreatedTripWhenGetPickUpThenItShouldBePresent") {
    val trip = Trip(pickUpEvent)
    trip.pickUp.timestamp shouldNot be(null)
  }
}