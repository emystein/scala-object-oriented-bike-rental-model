package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenGenerator, TokenRegistry}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class BikePickUpEventTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var tokenRegistry: TokenRegistry = null
  private var reservedToken: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedToken = tokenRegistry.reserveTokenForUser(user)
  }

  test("givenAReservedTokenWhenCreateBikePickUpEventThenTheBikePickUpEventShouldHaveTheReservedTokenOwner") {
    val event = BikePickUpEvent(bike1, reservedToken)
    event.user should be(reservedToken.owner)
  }

  test("givenABikeWhenCreateBikePickUpEventThenTheBikePickUpEventShouldHaveTheBike") {
    val event = BikePickUpEvent(bike1, reservedToken)
    event.bike should be(bike1)
  }
}