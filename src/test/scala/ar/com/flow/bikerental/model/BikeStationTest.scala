package ar.com.flow.bikerental.model

trait BikeStationTestMethods {
  this: BikeStation =>

  def fillWithParkedBikes(): Unit = this.anchorages.foreach(_.parkBike(new Bike))
}

class BikeStationTest extends BaseSpec {
  "Bike Station with available Anchorages" when {
    "a Bike is parked" should {
      "park Bike" in {
        val positionToParkBike = 1

        bikeStation.parkBikeOnAnchorage(bike1, positionToParkBike)

        bikeStation.isAnchorageAvailable(positionToParkBike) shouldBe false
      }
    }
  }

  "Bike Station with parked Bikes" when {
    "a parked Bike is picked up" should {
      "release the Bike" in {
        bikeStation.fillWithParkedBikes()

        bikeStation.pickupAvailableBike(reservedToken) shouldBe defined

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
