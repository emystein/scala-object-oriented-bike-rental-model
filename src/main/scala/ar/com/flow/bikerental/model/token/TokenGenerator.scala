package ar.com.flow.bikerental.model.token

import java.time.Period
import java.time.LocalDateTime.now

import scala.util.Random

class TokenGenerator(val random: Random = new Random) {
  def generateTokenValidForPeriod(period: Period) = new RentToken(value = random.nextLong, expiration = now.plus(period), null)
}