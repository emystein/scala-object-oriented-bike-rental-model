package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenGenerator, TokenRegistry}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Random

class BikeMaintenanceTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var tokenRegistry: TokenRegistry = null
  private var bikeShop: BikeShop = null
  private var trips: TripRegistry = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
    trips = TripRegistry(tripCompletionRules)
    station = new BikeStation(1, trips, bikeShop)
    anchorage = station.getFreeSpots.iterator.next
  }

  "Anchorage with parked bike" when {
    "request bike maintenance" should {
      "inform bike shop that bike needs maintenance" in {
        anchorage.parkBike(bike1)
        anchorage.requestBikeMaintenance()
        bikeShop.maintenancePickupRequests should contain(BikeMaintenanceRequest(bike1))
      }
      "reject bike pickup for trip" in {
        anchorage.parkBike(bike1)
        anchorage.requestBikeMaintenance()
        anchorage.releaseBike(reservedRentToken1) shouldBe None
      }
    }
  }
  "Anchorage with parked bike and Maintenance Token for parked bike" when {
    "pickup bike for maintenance" should {
      "release the bike" in {
        anchorage.parkBike(bike1)
        bikeShop.requestMaintenance(Some(bike1))
        val bike = pickupBikeForMaintenance(bike1)
        bike shouldBe Some(bike1)
        anchorage.parkedBike shouldBe None
      }
    }
  }
  "Anchorage with parked bike and Maintenance Token for a different bike" when {
    "pickup bike for maintenance" should {
      "not release the bike" in {
        anchorage.parkBike(bike1)
        bikeShop.requestMaintenance(Some(bike1))
        val bike = pickupBikeForMaintenance(bike2)
        bike shouldBe None
        anchorage.parkedBike shouldBe Some(bike1)
      }
    }
  }
  "Bike Shop with active maintenance requests" when {
    "request next maintenance pickup token" should {
      "deliver token" in {
        bikeShop.requestMaintenance(Some(bike1))

        bikeShop.nextMaintenancePickupToken shouldBe Some(BikeMaintenanceToken(bike1))
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

  private def pickupBikeForMaintenance(bikeToPickup: Bike): Option[Bike] = {
    bikeShop.nextMaintenancePickupToken.filter(_.bike == bikeToPickup).flatMap(anchorage.releaseBike(_))
  }
}