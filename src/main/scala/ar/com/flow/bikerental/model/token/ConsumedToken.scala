package ar.com.flow.bikerental.model.token

import java.time.LocalDateTime

class ConsumedToken(val token: ReservedToken, val consumedAt: LocalDateTime) extends Token(token.value, token.expiration, token.owner)
