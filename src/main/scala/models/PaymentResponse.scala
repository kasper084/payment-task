package models

import java.time.LocalDateTime
import java.util.UUID

case class PaymentResponse(id: UUID,
                           fiatAmount: BigDecimal,
                           fiatCurrency: String,
                           coinAmount: BigDecimal,
                           coinCurrency: String,
                           exchangeRate: BigDecimal,
                           createdAt: LocalDateTime,
                           expirationTime: LocalDateTime)
