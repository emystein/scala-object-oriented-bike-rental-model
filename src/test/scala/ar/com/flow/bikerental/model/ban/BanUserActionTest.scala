package ar.com.flow.bikerental.model.ban

import ar.com.flow.bikerental.model.User
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BanUserActionTest extends AnyFunSuite with Matchers {
  private val user = new User(Some(1), "Emiliano Menéndez")

  test("givenAUserAndAnExecuteUserBanActionWhenExecuteThenTheUserBansShouldIncludeTheUser") {
    val userBanAction = new BanUser(user)
    userBanAction.execute
    user.ban shouldBe defined
  }
}