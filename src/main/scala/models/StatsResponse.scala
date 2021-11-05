package models

import models.PaymentRequest.{BigDecimalJsonFormat, jsonFormat5}

case class StatsResponse(paymentsCount: BigDecimal,
                         paymentsCountPerFiatCurrency: BigDecimal,
                         paymentsSumFiatAmount: BigDecimal,
                         paymentsSumCryptoAmount: BigDecimal,
                         paymentsEURValueSum: BigDecimal)

object StatsResponse {

  implicit val statsResponseFormat = jsonFormat5(StatsResponse.apply)
}