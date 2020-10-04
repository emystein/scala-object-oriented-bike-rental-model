package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BikeMaintenanceTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
    station = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop)
    anchorage = station.availableAnchorages.iterator.next
  }

  "Bike Shop with active maintenance requests" when {
    "request next maintenance pickup token" should {
      "deliver token" in {
        bikeShop.requestMaintenance(bike1)

        bikeShop.nextMaintenancePickupToken shouldBe Some(BikeMaintenanceToken(bike1))

        bikeShop.maintenanceRequests shouldBe empty
      }
    }
  }

  "Bike Shop without active maintenance requests" when {
    "request next maintenance pickup token" should {
      "deliver none" in {
        bikeShop.nextMaintenancePickupToken shouldBe None
      }
    }
  }
}