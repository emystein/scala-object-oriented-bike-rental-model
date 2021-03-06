package ar.com.flow.bikerental.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.Period

class UserTest extends AnyWordSpec with Matchers {
  "User" when {
     "is created" should {
       "not be banned" in {
         val user = User(Some(1), "Emiliano Menéndez")

         user.isBanned shouldBe false
       }
     }
    "is banned" should {
      "be banned" in {
        val user = User(Some(1), "Emiliano Menéndez")

        user.ban(Period.ofDays(2))

        user.ban.get.banPeriod shouldBe Period.ofDays(2)
      }
    }
  }
}
