package controllers

import akka.http.scaladsl.server.Route
import exceptions.Exception.ErrorInfo
import models.PaymentRequest.LongJsonFormat
import models.{PaymentRequest, PaymentResponse, StatsResponse}
import services.PaymentService
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.spray._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.Future

class PaymentController(paymentService: PaymentService) {

  private val newPaymentEndpoint: Endpoint[PaymentRequest, ErrorInfo, PaymentResponse, Any] =
    endpoint.post.in("payment" / "new").in(jsonBody[PaymentRequest]).out(jsonBody[PaymentResponse]).errorOut(jsonBody[ErrorInfo])

  val createNewPaymentRoute: Route =
    AkkaHttpServerInterpreter().toRoute(newPaymentEndpoint) { paymentRequest =>
      Future
        .successful(
          paymentService
            .addPayment(paymentRequest)
            .map(_.toPaymentResponse)
        )
    }

  private val paymentIDEndpoint: Endpoint[String, ErrorInfo, PaymentResponse, Any] = {
    endpoint.get.in("payment" / path[String]).out(jsonBody[PaymentResponse]).errorOut(jsonBody[ErrorInfo])

  }

  val paymentIDRoute: Route =
    AkkaHttpServerInterpreter().toRoute(paymentIDEndpoint) { id =>
      Future.successful(paymentService.isPaymentExists(id).map(_.toPaymentResponse))
    }

  private val paymentListByCurrency: Endpoint[String, ErrorInfo, List[PaymentResponse], Any] = {
    endpoint.get.in("payments").in(query[String]("currency"))
      .out(jsonBody[List[PaymentResponse]]).errorOut(jsonBody[ErrorInfo])

  }

  val paymentListRoute: Route =
    AkkaHttpServerInterpreter().toRoute(paymentListByCurrency) { currency =>
      Future.successful(paymentService.listOfPayments(currency).map(_.map(_.toPaymentResponse)))
    }

}
