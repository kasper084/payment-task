package services

import models.ConfigModel
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object ApiConf {

  val apiConf: ConfigModel = ConfigSource.default.load[ConfigModel] match {
    case Right(conf) => conf
    case Left(error) => throw new Exception(error.toString())
  }


}
