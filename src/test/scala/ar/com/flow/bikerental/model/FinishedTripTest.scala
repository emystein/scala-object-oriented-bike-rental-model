package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now
import java.time.{Duration, LocalDateTime}

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FinishedTripTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikePickup: BikePickUpEvent = null
  private var bikeDropOff: LocalDateTime = now

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    tripRegistry.clear()
    bikePickup = tripRegistry.startTrip(bike1, reservedToken).pickUp
  }

  "A finished trip" when {
    "get the duration" should {
      "return duration between pick-up and drop-off" in {
        val finishedTrip = new FinishedTrip(bikePickup, bikeDropOff)
        finishedTrip.duration shouldBe (Duration.between(bikePickup.timestamp, bikeDropOff))
      }
    }
  }
  "A finished trip that lasted 1 hour" when {
    "ask if lasted more than 2 hours" should {
      "return false" in {
        bikeDropOff = bikePickup.timestamp.plusHours(1)
        val finishedTrip = new FinishedTrip(bikePickup, bikeDropOff)
        finishedTrip.hasLastedMoreThan(Duration.ofHours(2)) shouldBe false
      }
    }
  }
  "A finished trip that lasted 3 hours" when {
    "ask if lasted more than 2 hours" should {
      "return true" in {
        bikeDropOff = bikePickup.timestamp.plusHours(3)
        val finishedTrip = new FinishedTrip(bikePickup, bikeDropOff)
        finishedTrip.hasLastedMoreThan(Duration.ofHours(2)) shouldBe true
      }
    }
  }
}