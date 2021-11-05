package services

import exceptions.Exception.ErrorInfo
import models.{Payment, PaymentRequest}
import utils.{DB, MarketData}

import java.time.LocalDateTime
import java.util.UUID

class PaymentService {
  private val repo = DB
  private val paymentConf = ApiConf.apiConf.api.payments

  def addPayment(paymentRequest: PaymentRequest): Either[ErrorInfo, Payment] = {

    import java.time.Duration._

    Either.cond(
      MarketData.exchangeRatesOfBTC.isDefinedAt(paymentRequest.fiatCurrency),
      {

        val exchangeRate = MarketData.exchangeRatesOfBTC(paymentRequest.fiatCurrency)
        val eurExchangeRate = MarketData.exchangeRatesToEUR(paymentRequest.fiatCurrency)

        val min = paymentConf.minEurAmount
        val max = paymentConf.maxEurAmount
        val eurAmount = paymentRequest.fiatAmount / eurExchangeRate

        val test = (min <= eurAmount && eurAmount <= max)

        Either.cond(
          test,
          {
            val coinAmount = paymentRequest.fiatAmount / exchangeRate

            val payment = Payment(
              id = UUID.randomUUID(),
              fiatAmount = paymentRequest.fiatAmount,
              fiatCurrency = paymentRequest.fiatCurrency,
              coinAmount = coinAmount,
              coinCurrency = paymentRequest.coinCurrency,
              exchangeRate = exchangeRate,
              eurExchangeRate = eurExchangeRate,
              createdAt = LocalDateTime.now,
              expirationTime = LocalDateTime.now plus ofMillis(paymentConf.expiration.toMillis)
            )

            repo.payments = payment :: repo.payments

            payment
          },
          ErrorInfo("Min/Max EUR rule is broken.")
        )
      },
      ErrorInfo("Not existing currency.")
    )
      .flatten
  }


}
