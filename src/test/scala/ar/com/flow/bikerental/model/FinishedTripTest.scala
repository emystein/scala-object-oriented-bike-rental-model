package ar.com.flow.bikerental.model

import java.time.{Duration, LocalDateTime}
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{ReservedToken, Token, TokenGenerator, TokenRegistry}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class FinishedTripTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikePickup: BikePickUpEvent = null
  private var bikeDropOff: LocalDateTime = now

  override protected def beforeEach(): Unit = {
    val tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    val reservedToken = new ReservedToken(new Token(1L, now.plusDays(1),  user), user, tokenRegistry)
    bikePickup = BikePickUpEvent(bike1, reservedToken)
  }

  test("givenAFinishedTripWhenGetTheRideDurationThenItShouldBeDurationBetweenPickUpAndDropOff") {
    val finishedTrip = new FinishedTrip(bikePickup, bikeDropOff)
    finishedTrip.duration shouldBe(Duration.between(bikePickup.timestamp, bikeDropOff))
  }

  test("givenAFinishedTripWhenAskTheRideLastedForMoreThan2HoursThenItShouldReturnFalse") {
    bikeDropOff = bikePickup.timestamp.plusHours(1)
    val finishedTrip = new FinishedTrip(bikePickup, bikeDropOff)
    finishedTrip.hasLastedMoreThan(Duration.ofHours(2)) shouldBe false
  }

  test("givenAFinishedTripWhenAskTheRideLastedForMoreThan2HoursThenItShouldReturnTrue") {
    bikeDropOff = bikePickup.timestamp.plusHours(3)
    val finishedTrip = new FinishedTrip(bikePickup, bikeDropOff)
    finishedTrip.hasLastedMoreThan(Duration.ofHours(2)) shouldBe true
  }
}