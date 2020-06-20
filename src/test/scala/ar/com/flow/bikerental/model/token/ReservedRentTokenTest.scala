package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.TestObjects
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ReservedRentTokenTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  override protected def beforeEach(): Unit = {
    tokenRegistry.deleteAll()
  }

  test("givenAReservedTokenWhenConsumeThenAConsumedTokenShouldBeCreated") {
    val reservedToken = new ReservedRentToken(new RentToken(value = 1L, expiration = LocalDateTime.now.plusDays(1), user), user, tokenRegistry)
    val consumedToken = reservedToken.consume
    consumedToken shouldNot be(null)
  }

  test("givenATokenRegistryAndAReservedTokenWhenConsumeThenAConsumedTokenShouldBeRegisteredInTheTokenRegistry") {
    val reservedToken = new ReservedRentToken(new RentToken(value = 1L, expiration = LocalDateTime.now.plusDays(1), user), user, tokenRegistry)
    val consumedToken = reservedToken.consume
    tokenRegistry.consumedTokens.getAll() should contain(consumedToken)
  }
}