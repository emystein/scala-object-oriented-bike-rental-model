package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime.now
import java.util.Random

import ar.com.flow.bikerental.model.User
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TokenRegistryTest extends AnyFunSuite with Matchers {
  private val tokenRegistry = new TokenRegistry(new TokenGenerator(new Random))
  private val user = new User("1", "Emiliano Menéndez")

  test("givenARegistryWhenReservingATokenThenTheTokenShouldBePresent") {
    val reservedToken = tokenRegistry.reserveTokenForUser(user)
    reservedToken shouldNot be(null)
  }

  test("givenAnUserWhenReserveATokenThenTheTokenShouldBeAssociatedToTheUser") {
    val token = tokenRegistry.reserveTokenForUser(user)
    token.owner shouldBe user
    tokenRegistry.tokensOf(user) should contain(token)
  }

  test("givenAnUserWhenReserveTwoTokensThenTheTokensShouldBeAssociatedToTheUser") {
    val token1 = tokenRegistry.reserveTokenForUser(user)
    val token2 = tokenRegistry.reserveTokenForUser(user)
    tokenRegistry.tokensOf(user) should contain allOf (token1, token2)
  }

  test("givenAnUserWhenReserveATokenThenTheTokenShouldHaveReservedTimestamp") {
    val token = tokenRegistry.reserveTokenForUser(user)
    token.reservedAt shouldNot be(null)
  }

  test("givenAReservedTokenWhenConsumingTheTokenThenThereShouldNoBeErrors") {
    val reservedToken = tokenRegistry.reserveTokenForUser(user)
    tokenRegistry.consumeToken(reservedToken)
  }

  test("givenAReservedTokenWhenConsumeTheTokenThenTheTokenShouldHaveConsumedTimestamp") {
    val reservedToken = tokenRegistry.reserveTokenForUser(user)
    val consumedToken = tokenRegistry.consumeToken(reservedToken)
    consumedToken.consumedAt shouldNot be(null)
  }

  test("givenAnExpiredTokenWhenConsumingTheTokenThenShouldOccurAnError") {
    val expiredToken = tokenRegistry.reserveTokenForUser(user)
    expiredToken.setExpiration(now.minusDays(1))
    assertThrows[IllegalArgumentException] {
      tokenRegistry.consumeToken(expiredToken)
    }
  }

  test("givenAReservedTokenWhenConsumingTheTokenForTheSecondTimeThenThereShouldOccurAnError") {
    val reservedToken = tokenRegistry.reserveTokenForUser(user)
    tokenRegistry.consumeToken(reservedToken)
    assertThrows[IllegalArgumentException] {
      tokenRegistry.consumeToken(reservedToken)
    }
  }
}