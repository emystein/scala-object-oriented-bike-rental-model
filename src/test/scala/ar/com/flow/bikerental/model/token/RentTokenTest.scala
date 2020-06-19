package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.User
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RentTokenTest extends AnyFunSuite with Matchers {
  private val user: User = new User("1", "Emiliano Menéndez")
   
  test("givenATransactionTokenWithExpirationDateInTheFutureWhenValidatingItThenItShouldBeValid") {
    val notExpiredToken = new RentToken(value = 1L, expiration = LocalDateTime.now.plusDays(1), user)
    notExpiredToken.hasExpired shouldBe false
  }

  test("givenATransactionTokenWithExpirationDateInThePastWhenValidatingItThenItShouldBeInvalid") {
    val expiredToken = new RentToken(value = 1L, expiration = LocalDateTime.now.minusDays(1), user)
    expiredToken.hasExpired shouldBe true
  }

  test("equals") {
    val token1 = new RentToken(value = 1L, expiration = LocalDateTime.now.plusDays(1), user)
    val token2 = new RentToken(value = 2L, expiration = LocalDateTime.now.plusDays(1), user)

    token1 shouldBe token1
    token2 shouldNot be(token1)
  }
}