package ar.com.flow.bikerental.model.ban

import ar.com.flow.bikerental.model.User
import ar.com.flow.bikerental.model.trip.completion.Pass
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PassTest extends AnyFunSuite with Matchers {
  private val user = new User(Some(1), "Emiliano Menéndez")

  test("givenAUserWhenExecuteByPassActionThenTheUserBansShouldNotIncludeTheUser") {
    val byPass = new Pass
    byPass.execute
    user.ban shouldNot be(defined)
  }
}