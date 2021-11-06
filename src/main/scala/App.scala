import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import controllers.PaymentController
import services.PaymentService

import scala.io.StdIn

object App {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem[Nothing](Behaviors.empty, "main-system")

    import system.executionContext

    val paymentService: PaymentService = new PaymentService

    val paymentController = new PaymentController(paymentService)

    val route = concat(
      paymentController.createNewPaymentRoute,
      paymentController.paymentIDRoute,
      paymentController.paymentListRoute,
      paymentController.statsRoute
    )

    val bindingFuture = Http().newServerAt("localhost", 9007).bind(route)

    println(s"Server now online")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}
