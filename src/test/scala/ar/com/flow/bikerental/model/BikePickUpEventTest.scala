package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedToken, TokenGenerator, TokenRegistry}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class BikePickUpEventTest extends AnyFunSuite with BeforeAndAfterEach with Matchers {
  private val user: User = new User("1", "Emiliano Menéndez")
  private var bike: Bike = null
  private var tokenRegistry: TokenRegistry = null
  private var reservedToken: ReservedToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    bike = Bike("1")
  }

  test("givenAReservedTokenWhenCreateBikePickUpEventThenTheBikePickUpEventShouldHaveTheReservedTokenOwner") {
    val event = BikePickUpEvent(bike, reservedToken)
    event.user should be(reservedToken.owner)
  }

  test("givenABikeWhenCreateBikePickUpEventThenTheBikePickUpEventShouldHaveTheBike") {
    val event = BikePickUpEvent(bike, reservedToken)
    event.bike should be(bike)
  }
}