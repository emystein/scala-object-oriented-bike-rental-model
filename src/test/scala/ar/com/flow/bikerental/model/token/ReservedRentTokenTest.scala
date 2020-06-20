package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.Random

import ar.com.flow.bikerental.model.User
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ReservedRentTokenTest extends AnyFunSuite with BeforeAndAfterEach with Matchers {
  private val user: User = User("1", "Emiliano Menéndez")
  private var tokenRegistry: TokenRegistry = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
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