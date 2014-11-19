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

trait TypedTag[T] extends Tag {
  val marker: Int
  val name: String
  def value(attr: IfdAttribute, order: ByteOrder): T
}

private[exif] class ValueTag[T : TypeConverter](marker: Int, name: String) extends TagImpl(marker, name) with TypedTag[T] {
  def value(attr: IfdAttribute, order: ByteOrder): T = {
    val genType = implicitly[TypeConverter[T]]
    genType.toScala(attr, order).head
  }
}

private[exif] class ListTag[T : TypeConverter](marker: Int, name: String) extends TagImpl(marker, name) with TypedTag[List[T]] {
  def value(attr: IfdAttribute, order: ByteOrder): List[T] = {
    val converter = implicitly[TypeConverter[T]]
    converter.toScala(attr, order)
  }
}

class ByteTag(marker: Int, name: String) extends ValueTag[ByteSeq](marker, name)

class UndefinedTag(marker: Int, name: String) extends ValueTag[ByteSeq](marker, name)

class AsciiTag(marker: Int, name: String) extends ValueTag[String](marker, name)

class LongListTag(marker: Int, name: String) extends ListTag[Long](marker, name)

class LongTag(marker: Int, name: String) extends ValueTag[Long](marker, name)

class ShortListTag(marker: Int, name: String) extends ListTag[Int](marker, name)

class ShortTag(marker: Int, name: String) extends ValueTag[Int](marker, name)

class RationalListTag(marker: Int, name: String) extends ListTag[Rational](marker, name)

class RationalTag(marker: Int, name: String) extends ValueTag[Rational](marker, name)

class SignedRationalTag(marker: Int, name: String) extends ValueTag[SignedRational](marker, name)

class NumericListTag(marker: Int, name: String) extends ListTag[Numeric](marker, name)

class NumericTag(marker: Int, name: String) extends ValueTag[Numeric](marker, name)

class UserCommentTag(marker: Int, name: String) extends ValueTag[MultiByteString](marker, name)
