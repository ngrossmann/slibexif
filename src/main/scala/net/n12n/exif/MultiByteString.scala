package net.n12n.exif

import java.nio.charset.Charset
import scala.language.implicitConversions

case class MultiByteString(value: String, charset: Charset) {
  override def toString = if (value == null) "null" else value.toString
}

object MultiByteString {
  implicit def tos(s: MultiByteString): String = s.value
}
