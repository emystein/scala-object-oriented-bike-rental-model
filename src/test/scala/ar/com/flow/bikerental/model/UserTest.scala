package ar.com.flow.bikerental.model

import org.scalatest.funsuite.AnyFunSuite
import java.time.Period

import org.scalatest.matchers.should.Matchers

class UserTest extends AnyFunSuite with Matchers {
    test("givenAnInitialStateWhenCreateAUserThenItShouldNotBeBanned") {
        val user = User(Some("1"), "Emiliano Menéndez")

        user.isBanned shouldBe false
    }

    test("givenANotBannedUserWhenBanUserForTwoDaysThenUserShouldBeBannedForTwoDays") {
        val user = User(Some("1"), "Emiliano Menéndez")

        user.ban(Period.ofDays(2))

        user.ban.get.period shouldBe Period.ofDays(2)
    }
}
