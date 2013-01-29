/* 
 * libexif - Scala library to parse JPEG EXIF data.
 * Copyright (C) Niklas Grossmann
 * 
 * This file is part of libexif.
 *
 * libexif is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * libexif is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with libexif.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.n12n.exif

class Type(val id: Int, val size: Int, val name: String)
/**
 * Simple types used in JPEG/EXIF.
 */
object Type {
  /** One byte value. */
  val Byte = new Type(1, 1, "BYTE")
  /* One byte character. */ 
  val Ascii = new Type(2, 1, "ASCII")
  /** Two byte unsigned integer. */
  val Short = new Type(3, 2, "SHORT")
  /** A 4 byte unsigned integer. */
  val Long = new Type(4, 4, "LONG")
  /** A rational value made up of two {{net.n12n.exif.Type.Long}} values. */ 
  val Rational = new Type(5, 8, "RATIONAL")
  /** Undefined type. */
  val Undefined = new Type(7, 1, "UNDEFINED")
  /** Signed 4 byte integer. */
  val SLong = new Type(9, 4, "SLONG")
  /** Signed rational, two 4 byte integers. */ 
  val SRational = new Type(10, 8, "SRATIONAL")
  /** Fall back in case type is not know. */
  val Unknown = new Type(11, 1, "UNKNOWN")
  
  val values = Seq(Byte, Ascii, Short, Long, Rational, Undefined, SLong, SRational)
  
  def value(id: Int): Type = {
    values.find(_.id == id) match {
      case Some(t) => t
      case None => Unknown
    }
  }
}
