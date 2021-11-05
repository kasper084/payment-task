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

  //if currency exists, min, max
  def addPayment(paymentRequest: PaymentRequest): Either[ErrorInfo, Payment] = {

    import java.time.Duration._

    val currencyCondition = Validated.cond(
      DB.fiatCurrencies.contains(paymentRequest.fiatCurrency) && DB.cryptoCurrencies.contains(paymentRequest.coinCurrency),
      (),
      ErrorInfo("CurrencyNotExist")
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
        ErrorInfo("MinMaxAmountError")
      )
    }

    (currencyCondition andThen amountCondition).toEither
  }


  //find by id and list by currency
  def isPaymentExists(id: String): Either[ErrorInfo, Payment] = {

    val uuid = UUID.fromString(id)

    Validated.cond(
      DB.payments.exists(_.id == uuid),
      DB.payments.find(_.id == uuid).get,
      ErrorInfo("PaymentNotExist")
    ).toEither

  }

  def listOfPayments(currency: String): Either[ErrorInfo, List[Payment]] = {

    Validated
      .cond(
        DB.payments.exists(_.fiatCurrency == currency),
        DB.payments.filter(_.fiatCurrency == currency),
        ErrorInfo("CurrencyNotExist")
      ).toEither
  }

  //stats

  def countAllPayments: Either[ErrorInfo, Long] = {

    Validated.cond(
      DB.payments.nonEmpty,
      DB.payments.size.toLong,
      ErrorInfo("NoStatsError")
    ).toEither
  }

  def paymentsCountPerFiatCurrency(currency: String): Either[ErrorInfo, Long] = {
    Validated
      .cond(
        DB.payments.exists(_.fiatCurrency == currency),
        DB.payments.count(_.fiatCurrency == currency).toLong,
        ErrorInfo("CurrencyNotExist")
      ).toEither
  }

/*  def paymentsSumFiatAmount(amount: BigDecimal): Either[ErrorInfo, BigDecimal] = {

    EUR sum + USD sum / ex.rate

    Validated
      .cond(
        DB.payments.exists(_.fiatAmount == amount),
        DB.payments.filter(_.fiatAmount == amount),
        ErrorInfo("NoOperationsWithSuchAmount")
      ).toEither
  }*/

// def paymentsSumCryptoAmount

  def paymentsEURValueSum: Either[ErrorInfo, BigDecimal] = {
    val eur = "EUR"

    Validated
      .cond(
        DB.payments.exists(_.fiatCurrency == eur),
        DB.payments.filter(_.fiatCurrency == eur).map(_.fiatAmount).sum,
        ErrorInfo("NoOperationsWithSuchAmount")
      ).toEither
  }


}
