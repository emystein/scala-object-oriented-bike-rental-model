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
  private var pickUpEvent: BikePickUpEvent = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    pickUpEvent = BikePickUpEvent(bike1, reservedToken)
  }

  test("givenAnInitialStateWhenCreateTripThenPickUpShouldBePresent") {
    trip = Trip(pickUpEvent, tripCompletionRules)
    trip.pickUp shouldNot be(null)
  }

  test("givenACreatedTripWhenGetPickUpThenItShouldBePresent") {
    trip = Trip(pickUpEvent, tripCompletionRules)
    trip.pickUp.timestamp shouldNot be(null)
  }
}