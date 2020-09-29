package ar.com.flow.bikerental.model.ban

import ar.com.flow.bikerental.model.User
import ar.com.flow.bikerental.model.trip.completion.Pass
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PassTest extends AnyWordSpec with Matchers {
  "Pass" when {
    "execute on User" should {
      "allow the User" in {
        val user = User(Some(1), "Emiliano Menéndez")

        val byPass = new Pass
        byPass.execute
        user.ban shouldNot be(defined)
      }
    }
  }
}