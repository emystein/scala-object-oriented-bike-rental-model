package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BikeMaintenanceBikeAnchorageTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    bikeShop = new BikeShop()
    anchorage = new BikeAnchorage(tripRegistry, bikeShop)
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    anchorage.parkBike(bike1)
  }

  "Anchorage with parked Bike" when {
    "request Bike maintenance" should {
      "inform Bike Shop that Bike needs maintenance" in {
        anchorage.requestBikeMaintenance()

        bikeShop.hasBikeInMaintenance(bike1) shouldBe true
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

  private def pickupBikeForMaintenance(bikeToPickup: Bike): Option[Bike] = {
    bikeShop.nextMaintenancePickupToken().filter(_.bike == bikeToPickup).flatMap(anchorage.releaseBike(_))
  }
}