package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime.now
import java.time.Period
import java.util.Random

import ar.com.flow.bikerental.model.{TestObjects, User}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TokenRegistryTest extends AnyFunSuite with TestObjects with Matchers {
  test("givenATokenGeneratorWhenGeneratingANewTokenThenTheGeneratedTokenShouldBeValid") {
    val token = tokenRegistry.generateTokenValidForPeriod(Period.ofDays(1), user)
    token.value shouldNot be(null)
    token.hasExpired shouldBe false
    token.owner shouldBe user
  }

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

  test("givenATokenStoredInTheRegistryWhenGetTokenByValueThenTheRegistryShouldReturnToken") {
    val reservedToken = tokenRegistry.reserveTokenForUser(user)

    val tokenByValue = tokenRegistry.getTokenByValue(reservedToken.value)

    tokenByValue shouldBe Some(reservedToken)
  }
}