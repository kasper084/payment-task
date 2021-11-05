package models

import spray.json.DefaultJsonProtocol
import utils.helpers.{LocalTimeHelper, UUIDHelper}

import java.time.LocalDateTime
import java.util._

case class PaymentResponse(id: UUID,
                           fiatAmount: BigDecimal,
                           fiatCurrency: String,
                           coinAmount: BigDecimal,
                           coinCurrency: String,
                           exchangeRate: BigDecimal,
                           eurExchangeRate: BigDecimal,
                           createdAt: LocalDateTime,
                           expirationTime: LocalDateTime)


object PaymentResponse extends DefaultJsonProtocol
  with UUIDHelper
  with LocalTimeHelper {

  implicit val paymentResponseFormat = jsonFormat9(PaymentResponse.apply)

}
