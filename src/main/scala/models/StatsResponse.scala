package models

import models.PaymentRequest.{BigDecimalJsonFormat, LongJsonFormat, jsonFormat5}

case class StatsResponse(paymentsCount: Long,
                         paymentsCountPerFiatCurrency: Long,
                         paymentsSumFiatAmount: BigDecimal,
                         paymentsSumCryptoAmount: BigDecimal,
                         paymentsEURValueSum: BigDecimal)

object StatsResponse {

  implicit val statsResponseFormat = jsonFormat5(StatsResponse.apply)
}