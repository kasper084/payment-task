package models

import scala.concurrent.duration.FiniteDuration

case class PaymentConfigModel(expiration: FiniteDuration,
                              minEurAmount: BigDecimal,
                              maxEurAmount: BigDecimal)

case class ApiConfigModel(payments: PaymentConfigModel)

case class ConfigModel(api: ApiConfigModel)
