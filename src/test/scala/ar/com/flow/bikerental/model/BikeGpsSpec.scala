package ar.com.flow.bikerental.model

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BikeGpsSpec extends AnyWordSpec with TestObjects with BeforeAndAfterEach with Matchers {
  override protected def beforeEach(): Unit = {
    super.beforeEach()
    anchorage.parkBike(bike1)
  }

  "Anchorage with parked Bike" when {
    "request Bike maintenance" should {
      "inform Bike Shop bike location" in {
        gps.informLocation(bike1, bikeStation)

        gps.bikeLocation(bike1) shouldBe Some(bikeStation)
      }
    }
  }
}
