package ar.com.flow.bikerental.model.ban

import java.time.LocalDateTime
import java.time.LocalDateTime.now

import ar.com.flow.bikerental.model.token.{ReservedToken, Token, TokenGenerator, TokenRegistry}
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripCompletionRulesFactory}
import ar.com.flow.bikerental.model.{Bike, BikePickUpEvent, FinishedTrip, User}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class TripCompletionRulesTest extends AnyFunSuite with BeforeAndAfterEach with Matchers {
  private var bikeTripCompleteChecker: TripCompletionRules = null
  private val user: User = new User("1", "Emiliano Menéndez")
  private var bike: Bike = null
  private val mondayAt10Am = LocalDateTime.of(2019, 12, 2, 10, 0)
  private val sundayAt10Am = LocalDateTime.of(2019, 12, 1, 10, 0)
  private var reservedToken: ReservedToken = null
  private var bikePickup: BikePickUpEvent = null
  private var bikeDropOff: LocalDateTime = null
  private var completedTrip: FinishedTrip = null

  override protected def beforeEach(): Unit = {
    bikeTripCompleteChecker = TripCompletionRulesFactory.create
    bike = Bike("1")
    val tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedToken = new ReservedToken(Token(value=1L, expiration = now.plusDays(1), user), user, tokenRegistry)
    bikeDropOff = now
  }

  test("givenARentedBikeOnWeekdayWhenReturnTheBikeBefore1HourThenTheBanRulesShouldNotApply") {
    bikePickup = BikePickUpEvent(bike, reservedToken)
    bikePickup.timestamp = mondayAt10Am.minusMinutes(59)

    bikeDropOff = mondayAt10Am

    completedTrip = new FinishedTrip(bikePickup, bikeDropOff)

    val result = bikeTripCompleteChecker.test(completedTrip)

    result.userIsBanned shouldBe false
  }

  test("givenARentedBikeOnWeekdayWhenReturnTheBikeAfter1HourThenTheUserShouldGetBanned") {
    bikePickup = BikePickUpEvent(bike, reservedToken)
    bikePickup.timestamp = mondayAt10Am.minusHours(2)

    bikeDropOff = mondayAt10Am

    completedTrip = new FinishedTrip(bikePickup, bikeDropOff)

    val result = bikeTripCompleteChecker.test(completedTrip)

    result.userIsBanned shouldBe true
  }

  test("givenARentedBikeOnWeekendWhenReturnTheBikeBefore2HoursThenTheBanRulesShouldNotApply") {
    bikePickup = BikePickUpEvent(bike, reservedToken)
    bikePickup.timestamp = sundayAt10Am.minusMinutes(119)

    bikeDropOff = sundayAt10Am

    completedTrip = new FinishedTrip(bikePickup, bikeDropOff)

    val result = bikeTripCompleteChecker.test(completedTrip)

    result.userIsBanned shouldBe false
  }

  test("givenARentedBikeOnWeekendWhenReturnTheBikeAfter2HoursThenTheUserShouldGetBanned") {
    bikePickup = BikePickUpEvent(bike, reservedToken)
    bikePickup.timestamp = sundayAt10Am.minusHours(3)

    bikeDropOff = sundayAt10Am

    completedTrip = new FinishedTrip(bikePickup, bikeDropOff)

    val result = bikeTripCompleteChecker.test(completedTrip)

    result.userIsBanned shouldBe true
  }
}