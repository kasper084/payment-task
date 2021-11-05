package services

import cats.data.Validated
import exceptions.Exception.ErrorInfo
import models.{Payment, PaymentRequest}
import utils.{DB, MarketData}

import java.time.LocalDateTime
import java.util.UUID

class PaymentService {

  private val db = DB
  private val paymentConf = ApiConf.apiConf.api.payments

  def addPayment(paymentRequest: PaymentRequest): Either[ErrorInfo, Payment] = {

    import java.time.Duration._

    val currencyCondition = Validated.cond(
      DB.fiatCurrencies.contains(paymentRequest.fiatCurrency) && DB.cryptoCurrencies.contains(paymentRequest.coinCurrency),
      (),
      ErrorInfo("CurrencyNotExisting")
    )

    val amountCondition = (_: Unit) => {
      val exchangeRate = MarketData.exchangeRatesOfBTC(paymentRequest.fiatCurrency)

      val eurExchangeRate = MarketData.exchangeRatesToEUR(paymentRequest.fiatCurrency)

      val min = paymentConf.minEurAmount
      val max = paymentConf.maxEurAmount
      val eurAmount = paymentRequest.fiatAmount / eurExchangeRate

      val test = min <= eurAmount && eurAmount <= max

      Validated.cond(
        test,
        {
          val coinAmount = paymentRequest.fiatAmount / exchangeRate

          val now = LocalDateTime.now

          val payment = Payment(
            id = UUID.randomUUID(),
            fiatAmount = paymentRequest.fiatAmount,
            fiatCurrency = paymentRequest.fiatCurrency,
            coinAmount = coinAmount,
            coinCurrency = paymentRequest.coinCurrency,
            exchangeRate = exchangeRate,
            eurExchangeRate = eurExchangeRate,
            createdAt = now,
            expirationTime = now plus ofMillis(paymentConf.expiration.toMillis)
          )

          db.payments = payment :: db.payments

          payment
        },
        ErrorInfo("Min/Max Amount Error.")
      )
    }

    (currencyCondition andThen amountCondition).toEither
  }

}
