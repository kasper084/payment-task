package models

case class PaymentRequest(fiatAmount: BigDecimal,
                          fiatCurrency: String,
                          coinCurrency: String)
