package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BikeMaintenanceTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  override protected def beforeEach(): Unit = {
    super.beforeEach()
    anchorage.parkBike(bike1)
  }

  "Anchorage with parked Bike" when {
    "request Bike maintenance" should {
      "inform Bike Shop that Bike needs maintenance" in {
        anchorage.requestBikeMaintenance()

        bikeShop.hasBikeInMaintenance(bike1) shouldBe true
      }
      "inform bike location" in {
        anchorage.requestBikeMaintenance()

        gps.bikeLocation(bike1) shouldBe Some(bikeStation)
      }
      "reject Bike Pickup for Trip" in {
        anchorage.requestBikeMaintenance()

        anchorage.releaseBike(reservedRentToken1) shouldBe None
      }
    }
    "pickup Bike for maintenance" should {
      "release the Bike" in {
        anchorage.requestBikeMaintenance()

        pickupBikeForMaintenance(bike1) shouldBe Some(bike1)

        anchorage.parkedBike shouldBe None
      }
    }
    "try to pickup not broken Bike for maintenance" should {
      "not release the Bike" in {
        anchorage.requestBikeMaintenance()

        pickupBikeForMaintenance(bike2) shouldBe None

        anchorage.parkedBike shouldBe Some(bike1)
      }
    }
  }

  "Bike Shop with active maintenance requests" when {
    "request next maintenance Pickup Token" should {
      "deliver Pickup Token" in {
        bikeShop.requestMaintenance(bike1, bikeStation)

        bikeShop.nextMaintenancePickupToken shouldBe Some(BikeMaintenanceToken(bike1))

        bikeShop.maintenanceRequests shouldBe empty
      }
    }
  }

  "Bike Shop without active maintenance requests" when {
    "request next maintenance Pickup Token" should {
      "deliver None" in {
        bikeShop.nextMaintenancePickupToken shouldBe None
      }
    }
  }

  private def pickupBikeForMaintenance(bikeToPickup: Bike): Option[Bike] = {
    bikeShop.nextMaintenancePickupToken().filter(_.bike == bikeToPickup).flatMap(anchorage.releaseBike(_))
  }
}