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

  test("givenATransactionTokenWithExpirationDateInTheFutureWhenValidatingItThenItShouldBeValid") {
    val notExpiredToken = new ReservedRentToken(value = "1", expiration = LocalDateTime.now.plusDays(1), owner = user, tokenRegistry = tokenRegistry)
    notExpiredToken.hasExpired shouldBe false
  }

  test("givenATransactionTokenWithExpirationDateInThePastWhenValidatingItThenItShouldBeInvalid") {
    val expiredToken = new ReservedRentToken(value = "1", expiration = LocalDateTime.now.minusDays(1), owner = user, tokenRegistry = tokenRegistry)
    expiredToken.hasExpired shouldBe true
  }

  test("equals") {
    val token1 = new ReservedRentToken(value = "1", expiration = LocalDateTime.now.plusDays(1), owner = user, tokenRegistry = tokenRegistry)
    val token2 = new ReservedRentToken(value = "2", expiration = LocalDateTime.now.plusDays(1), owner = user, tokenRegistry = tokenRegistry)

    token1 shouldBe token1
    token2 shouldNot be(token1)
  }
  
  test("givenAReservedTokenWhenConsumeThenAConsumedTokenShouldBeCreated") {
    val reservedToken = ReservedRentToken(expiration = LocalDateTime.now.plusDays(1), owner = user, tokenRegistry = tokenRegistry)
    val consumedToken = reservedToken.consume
    consumedToken shouldNot be(null)
  }

  test("givenATokenRegistryAndAReservedTokenWhenConsumeThenAConsumedTokenShouldBeRegisteredInTheTokenRegistry") {
    val reservedToken = ReservedRentToken(expiration = LocalDateTime.now.plusDays(1), owner = user, tokenRegistry = tokenRegistry)
    val consumedToken = reservedToken.consume
    tokenRegistry.consumedTokens.getAll() should contain(consumedToken)
  }
}