package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedToken
import ar.com.flow.bikerental.model.token.TokenGenerator
import ar.com.flow.bikerental.model.token.TokenRegistry
import ar.com.flow.bikerental.model.trip.completion.SuccessResult
import ar.com.flow.bikerental.model.trip.completion.TripCompletionRules
import ar.com.flow.bikerental.model.trip.completion.TripCompletionRulesFactory
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class TripTest extends AnyFunSuite with BeforeAndAfterEach with Matchers {
  private val user: User = User("1", "Emiliano Menéndez")
  private var bike: Bike = null
  private var trip: Trip = null
  private var tokenRegistry: TokenRegistry = null
  private var reservedToken: ReservedToken = null
  private var tripCompletionRules: TripCompletionRules = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    bike = Bike("1")
    tripCompletionRules = TripCompletionRulesFactory.create
    reservedToken = tokenRegistry.reserveTokenForUser(user)
  }

  test("givenAnInitialStateWhenCreateTripThenPickUpShouldBePresent") {
    trip = Trip(bike, reservedToken, tripCompletionRules)
    trip.pickUp shouldNot be(null)
  }

  test("givenACreatedTripWhenGetPickUpThenItShouldBePresent") {
    trip = Trip(bike, reservedToken, tripCompletionRules)
    trip.pickUp.timestamp shouldNot be(null)
  }

  test("givenAnInitialStateWhenCreateTripThenTripCompletionResultShouldNotBePresent") {
    trip = Trip(bike, reservedToken, tripCompletionRules)
    trip.completionResult shouldBe None
  }

  test("givenATripWhenFinishTripThenTheEventShouldBePresent") {
    trip = Trip(bike, reservedToken, tripCompletionRules)
    val tripCompletionResult = trip.finish
    tripCompletionResult shouldNot be(null)
  }

  test("givenATripWithPickUpEventWhenTripCompleteTripThenResultShouldBeSuccess") {
    trip = Trip(bike, reservedToken, tripCompletionRules)
    val tripCompletionResult = trip.finish
    tripCompletionResult.get.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
  }
}