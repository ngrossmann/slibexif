package net.n12n.exif

import scala.language.implicitConversions

/**
 * Numeric value.
 * Tags of this type can be either short or integer.
 * @param value value as long.
 */
case class Numeric(value: Long) {
  override def toString = value.toString
}

/**
 * Numeric value.
 */
object Numeric {
  implicit def toLong(num: Numeric): Long = num.value
}
