package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.trip.completion.SuccessResult
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

    val tripCompletionResult = tripRegistry.finish(trip)

    tripCompletionResult.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
  }

  test("givenABikeWhenFinishCurrentTripForBikeThenTheEventShouldBePresent") {
    val trip = tripRegistry.startTrip(bike1, reservedToken)

    val tripCompletionResult = tripRegistry.finishCurrentTripForBike(bike1)

    tripCompletionResult.get.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
  }

  test("givenANoCurrentTripForBikeWhenFinishCurrentTripForBikeThenItShouldReturnNone") {
    val tripCompletionResult = tripRegistry.finishCurrentTripForBike(bike1)

    tripCompletionResult shouldBe None
  }
}