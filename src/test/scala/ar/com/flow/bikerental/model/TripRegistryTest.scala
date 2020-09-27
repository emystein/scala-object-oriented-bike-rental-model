package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TripRegistryTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  override protected def beforeEach(): Unit = {
    reservedToken = tokenRegistry.reserveTokenForUser(user)
  }

  "Trip Registry" when {
    "finish trip" should {
      "success" in {
        val trip = tripRegistry.startTrip(bike1, reservedToken)

        tripRegistry.finishTrip(trip).isSuccess shouldBe true
      }
    }
    "finish current trip of a bike" should {
      "success" in {
        val trip = tripRegistry.startTrip(bike1, reservedToken)

        val tripCompletionResult = tripRegistry.finishCurrentTripForBike(bike1)

        tripCompletionResult.get.isSuccess shouldBe true
      }
    }
    "finish no current trip of a bike" should {
      "return None" in {
        tripRegistry.finishCurrentTripForBike(bike1) shouldBe None
      }
    }
  }
}