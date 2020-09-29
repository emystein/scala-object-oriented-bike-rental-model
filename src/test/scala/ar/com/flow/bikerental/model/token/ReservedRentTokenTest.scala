package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.TestObjects
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ReservedRentTokenTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
  }

  "ReservedRentToken" when {
    "has expiration date in the future" should {
      "be valid" in {
        val notExpiredToken = ReservedRentToken(value = "1", expiration = LocalDateTime.now.plusDays(1), owner = user)
        notExpiredToken.hasExpired shouldBe false
      }
    }
    "has expiration date in the past" should {
      "be valid" in {
        val expiredToken = ReservedRentToken(value = "1", expiration = LocalDateTime.now.minusDays(1), owner = user)
        expiredToken.hasExpired shouldBe true
      }
    }
    "consumed" should {
      "add a ConsumedRentToken to the TokenRegistry" in {
        val reservedToken = ReservedRentToken(expiration = LocalDateTime.now.plusDays(1), owner = user)
        val consumedToken = tokenRegistry.consumeToken(reservedToken)
        consumedToken shouldNot be(null)
        tokenRegistry.consumedTokens.getAll() should contain(consumedToken)
      }
    }
  }
}