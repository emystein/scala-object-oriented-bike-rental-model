package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.trip.completion.SuccessResult
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EmptyBikeAnchorageTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  "An empty Bike Anchorage" when {
    "ask for bike" should {
      "return None" in {
        anchorage.parkedBike shouldBe None
      }
    }
    "ask if it is locked" should {
      "return False" in {
        anchorage.hasParkedBike shouldBe false
      }
    }
    "park a bike" should {
      "lock" in {
        anchorage.parkBike(bike1)

        anchorage.hasParkedBike shouldBe true
      }
      "complete the trip" in {
        anchorage.parkBike(bike1)
        // Releasing a bike starts a trip
        anchorage.releaseBike(reservedRentToken1)
        val completedBikeTrip = anchorage.parkBike(bike1)
        val trip = tripRegistry.getCurrentTripForBike(bike1)

        val completedTrip = completedBikeTrip.get.completedTrip
        completedTrip.user shouldBe reservedRentToken1.owner
        completedTrip.bike shouldBe bike1
      }
      "execute post actions" in {
        anchorage.parkBike(bike1)
        anchorage.releaseBike(reservedRentToken1)
        val tripCompletionResult = anchorage.parkBike(bike1)

        tripCompletionResult shouldNot be(null)
        tripCompletionResult.get.rulesCheckResult.isInstanceOf[SuccessResult] shouldBe true
      }
    }
    "release the bike" should {
      "return None" in {
        anchorage.releaseBike(reservedRentToken1) shouldBe None
      }
    }
  }
}
