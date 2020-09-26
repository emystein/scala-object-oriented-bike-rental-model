package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import ar.com.flow.bikerental.model.trip.completion.SuccessResult
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EmptyBikeAnchorageTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    tripRegistry.clear()
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    bikeShop = new BikeShop()
    station = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop)
    anchorage = station.freeSpots.iterator.next
  }

  "An empty Bike Anchorage" when {
    "ask for bike" should {
      "return None" in {
        anchorage.parkedBike shouldBe None
      }
    }
    "ask if it is locked" should {
      "return False" in {
        anchorage.isLocked shouldBe false
      }
    }
    "park a bike" should {
      "lock" in {
        anchorage.parkBike(bike1)

        anchorage.isLocked shouldBe true
      }
      "complete the trip" in {
        anchorage.parkBike(bike1)
        // Releasing a bike starts a trip
        anchorage.releaseBike(reservedRentToken1)
        val completedBikeTrip = anchorage.parkBike(bike1)
        val trip = tripRegistry.getCurrentTripForBike(bike1)

        val completedTrip = completedBikeTrip.get.completedTrip
        completedTrip.user shouldBe (reservedRentToken1.owner)
        completedTrip.bike shouldBe (bike1)
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
