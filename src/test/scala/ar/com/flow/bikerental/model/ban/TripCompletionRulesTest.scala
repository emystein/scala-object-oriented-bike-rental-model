package ar.com.flow.bikerental.model.ban

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.{BikePickUpEvent, FinishedTrip, TestObjects}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TripCompletionRulesTest extends AnyFunSuite with TestObjects with BeforeAndAfterEach with Matchers {
  private val mondayAt10Am = LocalDateTime.of(2019, 12, 2, 10, 0)
  private val sundayAt10Am = LocalDateTime.of(2019, 12, 1, 10, 0)

  override protected def beforeEach(): Unit = {
    tokenRegistry.deleteAll
  }

  test("givenARentedBikeOnWeekdayWhenReturnTheBikeBefore1HourThenTheBanRulesShouldNotApply") {
    val bikePickup = BikePickUpEvent(bike1, reservedToken)
    bikePickup.timestamp = mondayAt10Am.minusMinutes(59)

    val result = tripCompletionRules.test(new FinishedTrip(bikePickup, dropOffTimestamp = mondayAt10Am))

    result.userIsBanned shouldBe false
  }

  test("givenARentedBikeOnWeekdayWhenReturnTheBikeAfter1HourThenTheUserShouldGetBanned") {
    val bikePickup = BikePickUpEvent(bike1, reservedToken)
    bikePickup.timestamp = mondayAt10Am.minusHours(2)

    val result = tripCompletionRules.test(new FinishedTrip(bikePickup, dropOffTimestamp = mondayAt10Am))

    result.userIsBanned shouldBe true
  }

  test("givenARentedBikeOnWeekendWhenReturnTheBikeBefore2HoursThenTheBanRulesShouldNotApply") {
    val bikePickup = BikePickUpEvent(bike1, reservedToken)
    bikePickup.timestamp = sundayAt10Am.minusMinutes(119)

    val result = tripCompletionRules.test(new FinishedTrip(bikePickup, dropOffTimestamp = sundayAt10Am))

    result.userIsBanned shouldBe false
  }

  test("givenARentedBikeOnWeekendWhenReturnTheBikeAfter2HoursThenTheUserShouldGetBanned") {
    val bikePickup = BikePickUpEvent(bike1, reservedToken)
    bikePickup.timestamp = sundayAt10Am.minusHours(3)

    val result = tripCompletionRules.test(new FinishedTrip(bikePickup, dropOffTimestamp = sundayAt10Am))

    result.userIsBanned shouldBe true
  }
}