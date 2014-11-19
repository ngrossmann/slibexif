package net.n12n.exif

import scala.language.implicitConversions

case class Numeric(value: Long)

object Numeric {
  implicit def toLong(num: Numeric): Long = num.value
}
