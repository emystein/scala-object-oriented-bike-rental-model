package ar.com.flow.bikerental.model

import java.time.{LocalDateTime, Period}

import ar.com.flow.bikerental.model.token.{ReservedToken, Token, TokenGenerator, TokenRegistry}
import org.scalatest.{BeforeAndAfterEach, WordSpec}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Random

class BikeMaintenanceRequestTest extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var tokenRegistry: TokenRegistry = null
  private var trips: TripRegistry = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedToken = null

  override protected def beforeEach(): Unit = {
    tokenRegistry = TokenRegistry(new TokenGenerator(new Random))
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    trips = new TripRegistry(tripCompletionRules)
    station = new BikeStation(1, trips)
    anchorage = station.getFreeSpots.iterator.next
  }

  "Empty anchorage" when {
    "request maintenance" should {
      "ignore" in {
        anchorage.requestMaintenance()

        anchorage.bikeAvailability shouldBe None
      }
    }
  }

  "Anchorage with parked bike" when {
    "request maintenance" should {
      "need maintenance" in {
        anchorage.parkBike(bike1)

        anchorage.requestMaintenance()

        anchorage.bikeAvailability shouldBe Some(NeedMaintenance())
      }
    }
  }
}