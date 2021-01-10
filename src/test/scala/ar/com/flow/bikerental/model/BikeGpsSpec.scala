package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.token.ReservedRentToken
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BikeGpsSpec extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  private var bikeShop: BikeShop = null
  private var station: BikeStation = null
  private var anchorage: BikeAnchorage = null
  private var reservedRentToken1: ReservedRentToken = null
  private var gps: BikeGps = null

  override protected def beforeEach(): Unit = {
    reservedRentToken1 = tokenRegistry.reserveTokenForUser(user)
    gps = BikeGps()
    bikeShop = new BikeShop(gps)
    station = BikeStation(Some("1"), anchorageCount = 1, tripRegistry, bikeShop, gps)
    anchorage = station.availableAnchorages.iterator.next
    anchorage.parkBike(bike1)
  }

  "Anchorage with parked Bike" when {
    "request Bike maintenance" should {
      "inform Bike Shop bike location" in {
        gps.informLocation(bike1, station)

        gps.bikeLocation(bike1) shouldBe Some(station)
      }
    }
  }
}
