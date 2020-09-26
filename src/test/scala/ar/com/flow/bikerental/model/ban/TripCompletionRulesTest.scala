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
    tokenRegistry.clear()
    tripRegistry.clear()
  }

  test("givenARentedBikeOnWeekdayWhenReturnTheBikeBefore1HourThenTheBanRulesShouldNotApply") {
    val bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
    bikePickup.timestamp = mondayAt10Am.minusMinutes(59)

    val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup.timestamp, dropOffTimestamp = mondayAt10Am))

    result.userIsBanned shouldBe false
  }

  test("givenARentedBikeOnWeekdayWhenReturnTheBikeAfter1HourThenTheUserShouldGetBanned") {
    val bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
    bikePickup.timestamp = mondayAt10Am.minusHours(2)

    val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup.timestamp, dropOffTimestamp = mondayAt10Am))

    result.userIsBanned shouldBe true
  }

  test("givenARentedBikeOnWeekendWhenReturnTheBikeBefore2HoursThenTheBanRulesShouldNotApply") {
    val bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
    bikePickup.timestamp = sundayAt10Am.minusMinutes(119)

    val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup.timestamp, dropOffTimestamp = sundayAt10Am))

    result.userIsBanned shouldBe false
  }

  test("givenARentedBikeOnWeekendWhenReturnTheBikeAfter2HoursThenTheUserShouldGetBanned") {
    val bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
    bikePickup.timestamp = sundayAt10Am.minusHours(3)

    val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup.timestamp, dropOffTimestamp = sundayAt10Am))

    result.userIsBanned shouldBe true
  }
}