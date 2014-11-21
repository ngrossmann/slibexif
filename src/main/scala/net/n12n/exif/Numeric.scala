package net.n12n.exif

import scala.language.implicitConversions

case class Numeric(value: Long) {
  override def toString = value.toString
}

object Numeric {
  implicit def toLong(num: Numeric): Long = num.value
}
