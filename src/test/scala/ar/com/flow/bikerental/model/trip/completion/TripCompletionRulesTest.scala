package ar.com.flow.bikerental.model.trip.completion

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.{FinishedTrip, TestObjects}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TripCompletionRulesTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private val mondayAt10Am = LocalDateTime.of(2019, 12, 2, 10, 0)
  private val sundayAt10Am = LocalDateTime.of(2019, 12, 1, 10, 0)

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
  }

  "On a Weekday" when {
    "a rented Bike is returned within 1 hour" should {
      "not ban User" in {
        var bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
        bikePickup = mondayAt10Am.minusMinutes(59)

        val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup, bikeDropOff = mondayAt10Am))

        result.userIsBanned shouldBe false
      }
    }
    "a rented Bike is returned after 1 hour" should {
      "ban User" in {
        var bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
        bikePickup = mondayAt10Am.minusHours(2)

        val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup, bikeDropOff = mondayAt10Am))

        result.userIsBanned shouldBe true
      }
    }
  }

  "During the Weekend" when {
    "a rented Bike is returned within 2 hours" should {
      "not ban User" in {
        var bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
        bikePickup = sundayAt10Am.minusMinutes(119)

        val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup, bikeDropOff = sundayAt10Am))

        result.userIsBanned shouldBe false
      }
    }
    "a rented Bike is returned after 2 hours" should {
      "ban User" in {
        var bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
        bikePickup = sundayAt10Am.minusHours(3)

        val result = tripCompletionRules.test(new FinishedTrip(user, bike1, bikePickup, bikeDropOff = sundayAt10Am))

        result.userIsBanned shouldBe true
      }
    }
  }
}
