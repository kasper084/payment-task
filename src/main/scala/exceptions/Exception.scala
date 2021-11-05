package exceptions

import spray.json.DefaultJsonProtocol

object Exception {
  case class ErrorInfo(error: String)

  object ErrorInfo extends DefaultJsonProtocol {

    implicit val errorInfoFormat = jsonFormat1(ErrorInfo.apply)

  }
}
