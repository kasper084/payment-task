package models

import java.time.LocalDateTime
import java.util.UUID

case class Payment(id: UUID,
                   fiatAmount: BigDecimal,
                   fiatCurrency: String,
                   coinAmount: BigDecimal,
                   coinCurrency: String,
                   exchangeRate: BigDecimal,
                   eurExchangeRate: BigDecimal,
                   createdAt: LocalDateTime,
                   expirationTime: LocalDateTime)
