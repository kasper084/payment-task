package utils.helpers

import spray.json.{JsString, JsValue, JsonFormat}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Try

trait LocalTimeHelper {
  private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

  implicit val localDate = new JsonFormat[LocalDateTime] {
    override def write(obj: LocalDateTime): JsValue = JsString(formatter.format(obj))

    override def read(json: JsValue): LocalDateTime = {
      json match {
        case JsString(lDString) =>
          Try(LocalDateTime.parse(lDString, formatter)).getOrElse(throw new RuntimeException())
        case _ => throw new RuntimeException()
      }
    }
  }

}
