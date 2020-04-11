package ar.com.flow.bikerental.model.token

import java.time.Period
import java.util.Random

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TokenGeneratorTest extends AnyFunSuite with Matchers {
  test("givenATokenGeneratorWhenGeneratingANewTokenThenTheGeneratedTokenShouldBeValid") {
    val rentTokenGenerator = new TokenGenerator(new Random)
    val token = rentTokenGenerator.generateTokenValidForPeriod(Period.ofDays(1))
    token.value shouldNot be(null)
    token.hasExpired shouldBe false
  }
}