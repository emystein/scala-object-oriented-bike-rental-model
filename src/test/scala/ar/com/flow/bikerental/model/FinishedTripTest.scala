package ar.com.flow.bikerental.model

import java.time.LocalDateTime.now
import java.time.{Duration, LocalDateTime}

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FinishedTripTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikePickUp: LocalDateTime = null
  private var bikeDropOff: LocalDateTime = now

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    tripRegistry.clear()
    bikePickUp = tripRegistry.startTrip(bike1, reservedToken).pickUp
  }

  "A finished trip" when {
    "get the duration" should {
      "return duration between pick-up and drop-off" in {
        val finishedTrip = new FinishedTrip(user, bike1, bikePickUp, bikeDropOff)
        finishedTrip.duration shouldBe (Duration.between(bikePickUp, bikeDropOff))
      }
    }
  }
  "A finished trip that lasted 1 hour" when {
    "ask if lasted more than 2 hours" should {
      "return false" in {
        bikeDropOff = bikePickUp.plusHours(1)
        val finishedTrip = new FinishedTrip(user, bike1, bikePickUp, bikeDropOff)
        finishedTrip.hasLastedMoreThan(Duration.ofHours(2)) shouldBe false
      }
    }
  }
  "A finished trip that lasted 3 hours" when {
    "ask if lasted more than 2 hours" should {
      "return true" in {
        bikeDropOff = bikePickUp.plusHours(3)
        val finishedTrip = new FinishedTrip(user, bike1, bikePickUp, bikeDropOff)
        finishedTrip.hasLastedMoreThan(Duration.ofHours(2)) shouldBe true
      }
    }
  }
}