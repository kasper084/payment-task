package models

import spray.json.DefaultJsonProtocol

case class PaymentRequest(fiatAmount: BigDecimal,
                          fiatCurrency: String,
                          coinCurrency: String)

object PaymentRequest extends DefaultJsonProtocol {

  implicit val paymentRequestFormat = jsonFormat3(PaymentRequest.apply)

}
