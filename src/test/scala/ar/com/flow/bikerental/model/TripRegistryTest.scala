package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedToken, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.SuccessResult
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class TripRegistryTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var tokenRegistry: TokenRegistry = null
  private var reservedToken: ReservedToken = null
  private var trips: TripRegistry = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    trips = TripRegistry(tripCompletionRules)
  }

  test("givenATripWhenFinishTripThenTheEventShouldBePresent") {
    val pickUpEvent = BikePickUpEvent(bike1, reservedToken)
    val trip = Trip(pickUpEvent, tripCompletionRules)

    val tripCompletionResult = trips.finish(trip)

    tripCompletionResult.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
  }

  test("givenABikeWhenFinishCurrentTripForBikeThenTheEventShouldBePresent") {
    val trip = trips.startTrip(bike1, reservedToken)

    val tripCompletionResult = trips.finishCurrentTripForBike(bike1)

    tripCompletionResult.get.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
  }

  test("givenANoCurrentTripForBikeWhenFinishCurrentTripForBikeThenItShouldReturnNone") {
    val tripCompletionResult = trips.finishCurrentTripForBike(bike1)

    tripCompletionResult shouldBe None
  }
}