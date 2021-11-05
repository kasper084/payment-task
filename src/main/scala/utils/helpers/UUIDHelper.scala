package utils.helpers

import spray.json.{JsString, JsValue, JsonFormat}

import java.util.UUID

trait UUIDHelper {

  implicit object UuidToJson extends JsonFormat[UUID] {
    def write(s: UUID) = JsString(s.toString())

    def read(value: JsValue) = value match {
      case JsString(s) => UUID.fromString(s)
      case s => throw new RuntimeException("Wrong UUID as JsString: " + s)
    }
  }

}
