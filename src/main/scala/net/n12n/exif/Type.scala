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

import ByteOrder._

/**
 * IFD attribute types.
 * @param id ID as defined in Exif specification.
 * @param size Size in bytes.
 * @param name Type name.
 * @param stringLike `true` indicates this type should always be treated as a sequence, e.g. like
 *   a string is a sequence of characters. Count 1 is just a special case of count n.
 */
abstract class Type(val id: Int, val size: Int, val name: String, val stringLike: Boolean = false) {
  type ScalaType
  
  def toScala(data: ByteSeq, offset: Int, order: ByteOrder): ScalaType
}

/**
 * Generic IFD attribute type.
 * @param id ID as defined in Exif specification.
 * @param size Size in bytes.
 * @param name Type name.
 * @param stringLike `true` indicates this type should always be treated as a sequence, e.g. like
 *   a string is a sequence of characters. Count 1 is just a special case of count n.
 * @param T The related Scala type.
 */
abstract class GenericType[T](id: Int, size: Int, name: String, stringLike: Boolean = false) 
  extends Type(id, size, name, stringLike) {
  override type ScalaType = T
}

/**
 * Simple types used in JPEG/EXIF.
 */
object Type {
  /** One byte value. */
  val Byte = new GenericType[ByteSeq](1, 1, "BYTE", true) {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.slice(offset, data.length)
  }
  /** One byte character. */ 
  val Ascii = new GenericType[String](2, 1, "ASCII", true) {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.zstring(offset)
  }
  /** Two byte unsigned integer. */
  val Short = new GenericType[Int](3, 2, "SHORT") {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toShort(offset, order)
  }
  
  /** A 4 byte unsigned integer. */
  val Long = new GenericType[Long](4, 4, "LONG") {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toLong(offset, order)
  }
  /** A rational value made up of two `net.n12n.exif.Type.Long` values. */ 
  val Rational = new GenericType[Rational](5, 8, "RATIONAL") {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toRational(offset, order)
  }
  /** Undefined type. */
  val Undefined = new GenericType[ByteSeq](7, 1, "UNDEFINED", true) {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.slice(offset, data.length)
  }
  /** Signed 4 byte integer. */
  val SLong = new GenericType[Int](9, 4, "SLONG") {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toSignedLong(offset, order)
  }
  /** Signed rational, two 4 byte integers. */ 
  val SRational = new GenericType[SignedRational](10, 8, "SRATIONAL") {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toSignedRational(offset, order)
  }
  /** Fall back in case type is not know. */
  val Unknown = new GenericType[ByteSeq](11, 1, "UNKNOWN", true) {
    override def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.slice(offset, data.length)
  }
  
  val values = Seq(Byte, Ascii, Short, Long, Rational, Undefined, SLong, SRational)
  
  def value(id: Int): Type = {
    values.find(_.id == id) match {
      case Some(t) => t
      case None => Unknown
    }
  }
}
