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

/**
 * Tag identifying IFD attributes.
 * @param T The Scala type to which the tag is mapped.
 */
trait TypedTag[T] extends Tag {
  def value(attr: IfdAttribute, order: ByteOrder): T
  def ifdAttribute(tagType: Type, count: Int, data: ByteSeq, order: ByteOrder) =
    new GenericIfdAttribute(this, tagType, count, data, order)
}

trait GenericTag[T] extends TypedTag[T] {
  val tagType: GenericType[T]
  /**
   * Read value from `IfdAttribute`.
   * @throws IllegalArgumentException if count is not 1 or [[net.n12n.exif.Type.stringLike]] is
   *   `true`.
   */
  override def value(attr: IfdAttribute, order: ByteOrder): T = {
    require(tagType == attr.tagType)
    require(attr.count == 1 || tagType.stringLike)
    tagType.toScala(attr.data, 0, order)
  }  
}

trait GenericListTag[T] extends TypedTag[List[T]] {
  val tagType: GenericType[T]
  override def value(attr: IfdAttribute, order: ByteOrder): List[T] = {
    require(tagType == attr.tagType)
    (for (i <- 0 until attr.count) yield tagType.toScala(attr.data, i * tagType.size, order)).toList
  }
}

trait ByteTag extends GenericTag[ByteSeq] {
  override val tagType = Type.Byte
}

trait UndefinedTag extends GenericTag[ByteSeq] {
  override val tagType = Type.Undefined
}

trait AsciiTag extends GenericTag[String] {
  override val tagType = Type.Ascii
}

trait LongListTag extends GenericListTag[Long] {
  override val tagType = Type.Long  
}

trait LongTag extends GenericTag[Long] {
  override val tagType = Type.Long
}

trait ShortListTag extends GenericListTag[Int] {
  override val tagType = Type.Short
}

trait ShortTag extends GenericTag[Int] {
  override val tagType = Type.Short
}

trait RationalListTag extends GenericListTag[Rational] {
  override val tagType = Type.Rational
}

trait RationalTag extends GenericTag[Rational] {
  override val tagType = Type.Rational
}

trait SignedRationalTag extends GenericTag[SignedRational] {
  override val tagType = Type.SRational
}

trait NumericListTag extends TypedTag[List[Long]] {
  val tagType = Type.Long
  
  override def value(attr: IfdAttribute, order: ByteOrder) = {
    if (attr.tagType == Type.Short) {
      (for (i <- 0 until attr.count) yield 
          attr.data.toShort(i * Type.Short.size, order).toLong).toList
    } else if (attr.tagType == Type.Long) {
    	(for (i <- 0 until attr.count) yield attr.data.toLong(i * Type.Long.size, order)).toList
    } else
      throw new IllegalArgumentException("Type %s not supported".format(attr.tagType))
  }
}

trait NumericTag extends TypedTag[Long] {
  override def value(attr: IfdAttribute, order: ByteOrder) = {
    require(attr.count == 1)
    if (attr.tagType == Type.Short)
      attr.data.toShort(0, order)
    else if (attr.tagType == Type.Long)
      attr.data.toLong(0, order)
    else
      throw new IllegalArgumentException("Type %s not supported".format(attr.tagType))
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
