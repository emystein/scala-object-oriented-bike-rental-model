package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.{ReservedToken, TokenGenerator, TokenRegistry}
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
  private var reservedRentToken1: ReservedToken = null

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
    }
    "pickup bike for maintenance" should {
      "release the bike" in {
        anchorage.parkBike(bike1)

        val maintenanceRequest = anchorage.requestBikeMaintenance()

        val pickupToken = bikeShop.getMaintenancePickupToken(maintenanceRequest)

        val bike = anchorage.releaseBike(pickupToken.get)

        BikeLocationRegistry.relativeLocationOf(bike.get) shouldBe InTransitToShop()
      }
    }
    "try to pickup bike using maintenance request for another bike" should {
      "not release the bike" in {
        anchorage.parkBike(bike1)

        val maintenanceRequestForAnotherBike = Some(BikeMaintenanceRequest(bike2))

        val pickupToken = bikeShop.getMaintenancePickupToken(maintenanceRequestForAnotherBike)

        val bike = anchorage.releaseBike(pickupToken.get)

        bike shouldBe None
      }
    }
  }
}