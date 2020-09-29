package ar.com.flow.bikerental.model

import java.time.Period

import ar.com.flow.bikerental.model.token.ReservedRentToken
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BikeAnchorageTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry.clear()
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    anchorage = new BikeAnchorage(tripRegistry,new BikeShop())
  }

  "Available Bike Anchorage" when {
    "park a Bike" should {
      "receive the Bike" in {
        anchorage.parkBike(bike1)

        anchorage.parkedBike shouldBe Some(bike1)
      }
    }
  }

  "Occupied Bike Anchorage" when {
    "park a Bike" should {
      "reject the Bike" in {
        anchorage.parkBike(bike1)

        assertThrows[IllegalArgumentException] {
          anchorage.parkBike(bike2)
        }
      }
    }
    "pickup a Bike" should {
      "become available" in {
        anchorage.parkBike(bike1)

        anchorage.releaseBike(reservedRentToken1) shouldBe Some(bike1)

        anchorage.parkedBike shouldBe None
      }
    }
    "pickup a Bike using an expired Token" should {
      "throw an Exception" in {
        anchorage.parkBike(bike1)

        assertThrows[IllegalArgumentException] {
          anchorage.releaseBike(expiredToken)
        }
      }
    }
    "pickup a Bike using an already used Token" should {
      "throw an Exception" in {
        anchorage.parkBike(bike1)
        anchorage.releaseBike(reservedRentToken1)
        anchorage.parkBike(bike1)

        assertThrows[IllegalArgumentException] {
          anchorage.releaseBike(reservedRentToken1)
        }
      }
    }
    "pickup a Bike using a Token from a banned User" should {
      "throw an Exception" in {
        anchorage.parkBike(bike1)
        reservedRentToken1.owner.ban(Period.ofDays(2))

        assertThrows[IllegalArgumentException] {
          anchorage.releaseBike(reservedRentToken1)
        }
      }
    }
  }
}