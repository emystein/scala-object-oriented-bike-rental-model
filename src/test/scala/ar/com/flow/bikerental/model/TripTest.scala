package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedToken, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.SuccessResult
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class TripTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var trip: Trip = null
  private var tokenRegistry: TokenRegistry = null
  private var reservedToken: ReservedToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedToken = tokenRegistry.reserveTokenForUser(user)
  }

  test("givenAnInitialStateWhenCreateTripThenPickUpShouldBePresent") {
    trip = Trip(bike1, reservedToken, tripCompletionRules)
    trip.pickUp shouldNot be(null)
  }

  test("givenACreatedTripWhenGetPickUpThenItShouldBePresent") {
    trip = Trip(bike1, reservedToken, tripCompletionRules)
    trip.pickUp.timestamp shouldNot be(null)
  }

  test("givenATripWhenFinishTripThenTheEventShouldBePresent") {
    trip = Trip(bike1, reservedToken, tripCompletionRules)
    val tripCompletionResult = trip.finish
    tripCompletionResult shouldNot be(null)
  }

  test("givenATripWithPickUpEventWhenTripCompleteTripThenResultShouldBeSuccess") {
    trip = Trip(bike1, reservedToken, tripCompletionRules)
    val tripCompletionResult = trip.finish
    tripCompletionResult.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
  }
}