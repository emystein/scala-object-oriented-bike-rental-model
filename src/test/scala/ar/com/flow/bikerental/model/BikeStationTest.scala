package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

trait BikeStationTestMethods {
  this: BikeStation =>

  def fillWithParkedBikes(): Unit = this.anchorages.foreach(_.parkBike(new Bike))
}

class BikeStationTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  "Bike Station with available Anchorages" when {
    "ask for available Anchorage" should {
      "return first available" in {
        bikeStation.firstAvailableAnchorage shouldBe defined
      }
    }
    "a Bike is parked" should {
      "park Bike" in {
        bikeStation.parkBikeOnAnchorage(bike1, anchoragePosition = 1)

        bikeStation.availableAnchorages should have size 1
        bikeStation.occupiedAnchorages should have size 1
      }
    }
  }

  "Bike Station with parked Bikes" when {
    "a parked Bike is picked up" should {
      "release the Bike" in {
        bikeStation.fillWithParkedBikes()

        bikeStation.pickupAvailableBike(reservedToken) should be(defined)

        bikeStation.availableAnchorages should have size 1
      }
    }
    "a parked Bike is picked up using a used Token" should {
      "retain the Bike" in {
        bikeStation.fillWithParkedBikes()

        bikeStation.pickupAvailableBike(reservedToken)

        // try to use valid rent token for the second time
        assertThrows[IllegalArgumentException] {
          bikeStation.pickupAvailableBike(reservedToken)
        }
      }
    }
  }

  "Bike Station with all Anchorages occupied" when {
    "ask for available Anchorages" should {
      "return Empty" in {
        bikeStation.fillWithParkedBikes()

        bikeStation.availableAnchorages should be(Nil)
        bikeStation.occupiedAnchorages should have size 2
        bikeStation.firstAvailableAnchorage shouldBe None
      }
    }
    "pickup a Bike" should {
      "not release any Bike" in {
        val bike = bikeStation.pickupAvailableBike(reservedToken)

        bike shouldNot be(defined)
      }
    }
  }
}
