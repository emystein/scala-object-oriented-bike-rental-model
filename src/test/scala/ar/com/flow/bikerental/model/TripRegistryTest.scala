package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TripRegistryTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  override protected def beforeEach(): Unit = {
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    tripRegistry.clear()
  }

  test("givenATripWhenFinishTripThenTheEventShouldBePresent") {
    val trip = tripRegistry.startTrip(bike1, reservedToken)

    tripRegistry.finishTrip(trip).isSuccess shouldBe true
  }

  test("givenABikeWhenFinishCurrentTripForBikeThenTheEventShouldBePresent") {
    val trip = tripRegistry.startTrip(bike1, reservedToken)

    val tripCompletionResult = tripRegistry.finishCurrentTripForBike(bike1)

    tripCompletionResult.get.isSuccess shouldBe true
  }

  test("givenANoCurrentTripForBikeWhenFinishCurrentTripForBikeThenItShouldReturnNone") {
    tripRegistry.finishCurrentTripForBike(bike1) shouldBe None
  }
}