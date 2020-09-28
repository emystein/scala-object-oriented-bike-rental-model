package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BikeStationTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    reservedToken = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
  }

  "Bike Station" when {
    "is asked for valid Anchorage by ID" should {
      "return Anchorage" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
        bikeStation.getAnchorageById(1) should be(defined)
        bikeStation.getAnchorageById(2) should be(defined)
      }
    }
    "is asked for invalid Anchorage by ID" should {
      "return first Anchorage" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
        bikeStation.getAnchorageById(-1) shouldBe defined
        bikeStation.getAnchorageById(0) shouldBe defined
      }
    }
    "is asked for Anchorage by ID greater than present" should {
      "return None" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)

        bikeStation.getAnchorageById(3) shouldNot be(defined)
      }
    }
  }

  "Bike Station with available Anchorages" when {
    "is asked for available Anchorages" should {
      "return all Anchorages" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)

        bikeStation.availableAnchorages should have size 2
        bikeStation.occupiedAnchorages should be(Nil)
      }
    }
    "a Bike is parked" should {
      "park bike" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)

        bikeStation.parkBikeOnAnchorage(bike1, anchoragePosition = 1)

        bikeStation.occupiedAnchorages should have size 1
      }
    }
    "a Bike is picked up" should {
      "release the Bike" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop)
        fillStationWithBikes(bikeStation)

        val bike = bikeStation.pickupAvailableBike(reservedToken)
        bike should be(defined)
      }
    }
  }

  "Bike Station with parked Bikes" when {
    "a Bike is picked up" should {
      "decrement occupied Anchorages" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
        fillStationWithBikes(bikeStation)
        val rentToken = tokenRegistry.reserveTokenForUser(user)

        bikeStation.pickupAvailableBike(rentToken)

        bikeStation.occupiedAnchorages should have size 1
      }
    }
    "a Bike is picked up using a used Token" should {
      "not release the Bike" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop)
        fillStationWithBikes(bikeStation)

        // use valid rent token for the first time
        var bike = bikeStation.pickupAvailableBike(reservedToken)
        bike should be(defined)

        // try to use valid rent token for the second time
        bike = bikeStation.pickupAvailableBike(reservedToken)
        bike shouldNot be(defined)
      }
    }
  }

  "Bike Station with all Anchorages occupied" when {
    "is asked for available Anchorages" should {
      "return Empty" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 2, tripRegistry, bikeShop)
        fillStationWithBikes(bikeStation)

        bikeStation.availableAnchorages should be(Nil)
        bikeStation.occupiedAnchorages should have size 2
      }
    }
    "pickup a Bike" should {
      "not release any Bike" in {
        val bikeStation = BikeStation(Some("1"), anchorageCount = 0, tripRegistry, bikeShop)
        val bike = bikeStation.pickupAvailableBike(reservedToken)
        bike shouldNot be(defined)
      }
    }
  }

  private def fillStationWithBikes(bikeStation: BikeStation): Unit = bikeStation.anchorages.foreach(_.parkBike(new Bike))
}