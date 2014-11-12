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
 * net.n12n.exif is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with libexif.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.n12n.exif

import ByteOrder._
import java.nio.charset.Charset
import TypeConverter._

class TypedTag2[T : TypeConverter](marker: Int, name: String) extends Tag(marker, name) {
  def value(attr: IfdAttribute, order: ByteOrder): T = {
    val genType = implicitly[TypeConverter[T]]
    genType.toScala(attr, order)
  }
}

class RationalTag2[Rational](marker: Int, name: String) extends TypedTag2(marker, name)

/**
 * Tag identifying IFD attributes.
 * @param T The Scala type to which the tag is mapped.
 */
trait TypedTag[T] extends Tag {
  /**
   * Convert an `IfdAttribute` to its Scala representation.
   * @param attr attribute.
   * @param order byte order.
   * @return Scala value.
   */
  def value(attr: IfdAttribute, order: ByteOrder): T

}

trait ByteTag extends TypedTag[ByteSeq] {
  override def value(attr: IfdAttribute, order: ByteOrder): ByteSeq = attr.data.slice(0, attr.data.length)
}

trait UndefinedTag extends TypedTag[ByteSeq] {
  override def value(attr: IfdAttribute, order: ByteOrder): ByteSeq = attr.data.slice(0, attr.data.length)
}

trait AsciiTag extends TypedTag[String] {
  override def value(attr: IfdAttribute, order: ByteOrder): String = attr.data.zstring(0)
}

trait LongListTag extends TypedTag[List[Long]] {
  override def value(attr: IfdAttribute, order: ByteOrder): List[Long] =
    (for (i <- 0 until attr.count) yield attr.data.toLong(i * 4, order)).toList

}

trait LongTag extends TypedTag[Long] {
  override def value(attr: IfdAttribute, order: ByteOrder): Long = attr.data.toLong(0, order)
}

trait ShortListTag extends TypedTag[List[Int]] {
  override def value(attr: IfdAttribute, order: ByteOrder): List[Int] =
    (for (i <- 0 until attr.count) yield attr.data.toShort(i * 2, order)).toList
}

trait ShortTag extends TypedTag[Int] {
  override def value(attr: IfdAttribute, order: ByteOrder): Int = attr.data.toShort(0, order)
}

trait RationalListTag extends TypedTag[List[Rational]] {
  val Size = 8

  override def value(attr: IfdAttribute, order: ByteOrder): List[Rational] =
    (for (i <- 0 until attr.count) yield attr.data.toRational(i * Size, order)).toList
}

trait RationalTag extends TypedTag[Rational] {
  val Size = 8

  override def value(attr: IfdAttribute, order: ByteOrder): Rational = attr.data.toRational(0, order)
}

trait SignedRationalTag extends TypedTag[SignedRational] {

  override def value(attr: IfdAttribute, order: ByteOrder): SignedRational = attr.data.toSignedRational(0, order)
}

trait NumericListTag extends TypedTag[List[Long]] {

  override def value(attr: IfdAttribute, order: ByteOrder) = {
    if (attr.typeId == Type.Short.id) {
      (for (i <- 0 until attr.count) yield 
          attr.data.toShort(i * Type.Short.size, order).toLong).toList
    } else if (attr.typeId == Type.Long.id) {
    	(for (i <- 0 until attr.count) yield attr.data.toLong(i * Type.Long.size, order)).toList
    } else
      throw new IllegalArgumentException("Type %d not supported".format(attr.typeId))
  }
}

trait NumericTag extends TypedTag[Long] {
  override def value(attr: IfdAttribute, order: ByteOrder) = {
    require(attr.count == 1)
    if (attr.typeId == Type.Short.id)
      attr.data.toShort(0, order)
    else if (attr.typeId == Type.Long.id)
      attr.data.toLong(0, order)
    else
      throw new IllegalArgumentException("Type %d not supported".format(attr.typeId))
  }
}

trait UserCommentTag extends TypedTag[String] {
  
  override def value(attr: IfdAttribute, order: ByteOrder) = {
	  val charset = attr.data.zstring(0) match {
	    case "ASCII" => Charset.forName("US-ASCII")
	    case "JIS" => Charset.forName("")
	    case "Unicode" => Charset.forName("UTF-8")
	    case _ => Charset.defaultCharset()
	  }
	  new String(attr.data.slice(8).toArray, charset)
  }
}
