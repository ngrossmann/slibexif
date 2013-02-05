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

/**
 * Tag identifying IFD attributes.
 *
 */
trait TypedTag[T] extends Tag {
  def value(attr: IfdAttribute[_], order: ByteOrder): T
}

trait ByteTag extends TypedTag[ByteSeq] {
  val tagType = Type.Byte
  override def value(attr: IfdAttribute[_], order: ByteOrder) = attr.data
}

trait UndefinedTag extends TypedTag[ByteSeq] {
  val tagType = Type.Undefined
  override def value(attr: IfdAttribute[_], order: ByteOrder) = attr.data
}

trait AsciiTag extends TypedTag[String] {
  val tagType = Type.Ascii
  override def value(attr: IfdAttribute[_], order: ByteOrder) = attr.data.zstring(0)
}

trait LongListTag extends TypedTag[List[Long]] {
  val tagType = Type.Long
  
  override def value(attr: IfdAttribute[_], order: ByteOrder) = 
    (for (i <- 0 until attr.count) yield attr.data.toLong(i * tagType.size, order)).toList 
}

trait LongTag extends TypedTag[Long] {
  val tagType = Type.Long
  
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    require(attr.count == 1)
    attr.data.toLong(0, order)
  }
}

trait ShortListTag extends TypedTag[List[Int]] {
  val tagType = Type.Short
  
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    (for (i <- 0 until attr.count) yield attr.data.toShort(i * tagType.size, order)).toList
  }
}

trait ShortTag extends TypedTag[Int] {
  val tagType = Type.Short
  
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    require(attr.count == 1)
    attr.data.toShort(0, order)
  }
}

trait RationalListTag extends TypedTag[List[Rational]] {
  val tagType = Type.Rational
  
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    (for (i <- 0 until attr.count) yield attr.data.toRational(i * tagType.size, order)).toList
  }
}

trait RationalTag extends TypedTag[Rational] {
  val tagType = Type.Rational
  
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    require(attr.count == 1)
    attr.data.toRational(0, order)
  }
}

trait SignedRationalTag extends TypedTag[SignedRational] {
  val tagType = Type.SRational
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    require(attr.count == 1)
    attr.data.toSignedRational(0, order)
  }
}

trait NumericListTag extends TypedTag[List[Long]] {
  val tagType = Type.Long
  
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    (for (i <- 0 until attr.count) yield attr.data.toLong(i * tagType.size, order)).toList
  }
}

trait NumericTag extends TypedTag[Long] {
  override def value(attr: IfdAttribute[_], order: ByteOrder) = {
    require(attr.count == 1)
    if (attr.tagType == Type.Short)
      attr.data.toShort(0, order)
    else if (attr.tagType == Type.Long)
      attr.data.toLong(0, order)
    else
      throw new IllegalArgumentException("Type %s not supported".format(attr.tagType))
  }
}
