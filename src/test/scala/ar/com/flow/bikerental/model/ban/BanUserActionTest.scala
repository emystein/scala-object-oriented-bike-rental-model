package ar.com.flow.bikerental.model.ban

import ar.com.flow.bikerental.model.User
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BanUserActionTest extends AnyWordSpec with Matchers {
  "Ban Action" when {
    "execute on User" should {
      "ban the User" in {
        val user = User(Some(1), "Emiliano Menéndez")

        val userBanAction = new BanUser(user)
        userBanAction.execute
        user.ban shouldBe defined
      }
    }
  }
}