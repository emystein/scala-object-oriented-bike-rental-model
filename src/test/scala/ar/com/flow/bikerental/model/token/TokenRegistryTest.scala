package ar.com.flow.bikerental.model.token

import ar.com.flow.bikerental.model.TestObjects
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDateTime.now
import java.time.Period

class TokenRegistryTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
  }

  "TokenRegistry" when {
    "generate a new Token" should {
      "generate a valid Token" in {
        val token = tokenRegistry.generateTokenValidForPeriod(Period.ofDays(1), user)

        token.value shouldNot be(null)
        token.hasExpired shouldBe false
        token.owner shouldBe user
      }
    }
    "reserve a Token" should {
      "return reserved Token" in {
        val reservedToken = tokenRegistry.reserveTokenForUser(user)

        reservedToken shouldNot be(null)
      }
    }
    "reserve a Token" should {
      "associate the Token to the User" in {
        val token = tokenRegistry.reserveTokenForUser(user)
        token.owner shouldBe user
        tokenRegistry.tokensOf(user) should contain(token)
      }
    }
    "reserve two Tokens" should {
      "associate the Tokens to the User" in {
        val token1 = tokenRegistry.reserveTokenForUser(user)
        val token2 = tokenRegistry.reserveTokenForUser(user)
        tokenRegistry.tokensOf(user) should contain allOf(token1, token2)
      }
    }
    "reserve a Token" should {
      "set reservation timestamp" in {
        val token = tokenRegistry.reserveTokenForUser(user)
        token.reservedAt shouldNot be(null)
      }
    }
    "consume a Token" should {
      "set consumption timestamp" in {
        val reservedToken = tokenRegistry.reserveTokenForUser(user)
        val consumedToken = tokenRegistry.consumeToken(reservedToken)
        consumedToken.consumedAt shouldNot be(null)
      }
    }
    "consume an expired Token" should {
      "throw an Exception" in {
        val expiredToken = tokenRegistry.reserveTokenForUser(user)
        expiredToken.setExpiration(now.minusDays(1))
        assertThrows[IllegalArgumentException] {
          tokenRegistry.consumeToken(expiredToken)
        }
      }
    }
    "consume a Token twice" should {
      "throw an Exception" in {
        val reservedToken = tokenRegistry.reserveTokenForUser(user)
        tokenRegistry.consumeToken(reservedToken)
        assertThrows[IllegalArgumentException] {
          tokenRegistry.consumeToken(reservedToken)
        }
      }
    }
  }
}