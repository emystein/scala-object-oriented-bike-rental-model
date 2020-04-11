package ar.com.flow.bikerental.model

import java.time.{Duration, LocalDateTime}
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{ReservedToken, Token, TokenGenerator, TokenRegistry}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class CompletedTripTest extends AnyFunSuite with BeforeAndAfterEach with Matchers {
  private val user: User = User("1", "Emiliano Menéndez")
  private var bike: Bike = null
  private var bikePickup: BikePickUpEvent = null
  private var bikeDropOff: LocalDateTime = now

  override protected def beforeEach(): Unit = {
    val tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    val reservedToken = new ReservedToken(new Token(1L, now.plusDays(1),  user), user, tokenRegistry)
    bikePickup = BikePickUpEvent(bike, reservedToken)
    bike = Bike("1")
  }

  test("givenACompletedBikeRideWhenGetTheRideDurationThenItShouldBeDurationBetweenPickUpAndDropOff") {
    val completedBikeRide = new CompletedTrip(bikePickup, bikeDropOff)
    completedBikeRide.getDuration shouldBe(Duration.between(bikePickup.timestamp, bikeDropOff))
  }

  test("givenACompletedBikeRideWhenAskTheRideLastedForMoreThan2HoursThenItShouldReturnFalse") {
    bikeDropOff = bikePickup.timestamp.plusHours(1)
    val completedBikeRide = new CompletedTrip(bikePickup, bikeDropOff)
    completedBikeRide.hasLastedMoreThan(Duration.ofHours(2)) shouldBe false
  }

  test("givenACompletedBikeRideWhenAskTheRideLastedForMoreThan2HoursThenItShouldReturnTrue") {
    bikeDropOff = bikePickup.timestamp.plusHours(3)
    val completedBikeRide = new CompletedTrip(bikePickup, bikeDropOff)
    completedBikeRide.hasLastedMoreThan(Duration.ofHours(2)) shouldBe true
  }
}