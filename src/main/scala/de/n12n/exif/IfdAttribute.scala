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
package de.n12n.exif

object IfdAttribute {
  val Length = 12
  /**
   * Create a new IFD tag from EXIF data.
   * @param exif The Exif segment data.
   * @param tagOffset Start of the tag to be created relative to the tiffOffset.
   * @param tiffOffset Start of TIFF header in EXIF data.
   */
  def apply[T <: Tag](exif: ByteSeq, tagOffset: Int, tiffOffset: Int, order: ByteSeq,
      bytes2Tag: (Int) => T): 
    IfdAttribute[T] = {
    val start = tagOffset + tiffOffset
    val tag = bytes2Tag(exif.toShort(start, order))
    val tagType = Type.value(exif.toShort(start + 2, order))
    val count = exif.toSignedLong(start + 4, order)
    val size = tagType.size * count
    val dataOffset = if (size > 4)
        exif.toSignedLong(start + 8, order) + tiffOffset
      else
        start + 8
    create(tag, tagType, count, exif.slice(dataOffset, dataOffset + size), order) 
  }
  
  
  def create[T <: Tag](namedTag: T, tagType: Type, count: Int, data: ByteSeq, order: ByteSeq): 
    IfdAttribute[T] = {
    tagType match {
      case Type.Ascii => new AsciiIFD(namedTag, count, data)
      case Type.Byte => new ByteIFD(namedTag, count, data)
      case Type.Long => new LongIFD(namedTag, count, data, order)
      case Type.Rational => new RationalIFD(namedTag, count, data, order)
      case Type.Short => new ShortIFD(namedTag, count, data, order)
      case Type.SLong => new SignedLongIFD(namedTag, count, data, order)
      case Type.SRational => new SignedRationalIFD(namedTag, count, data, order)
      case Type.Undefined => new UndefinedIFD(namedTag, count, data)
      case _ => new UnknownIFD(namedTag, tagType, count, data)
    }
  }
}

abstract class IfdAttribute[T <: Tag](val tag: T, val tagType: Type, val count: Int, 
    val data: ByteSeq) {
  type V
  require(tagType.size * count == data.length)

  val value: V

  override def toString(): String = {
    val sval = value match {
      case seq: Seq[_] => "[" + seq.mkString(" ") + "]"
      case obj => obj.toString()
    }
    "%s(%s, %s)".format(getClass().getSimpleName(), tag, sval)
  }
}

class AsciiIFD[T <: Tag](tag: T, count: Int, data: ByteSeq)
  extends IfdAttribute(tag, Type.Ascii, count, data) {
  type V = String
  val value = data.zstring(0)
}

class ByteIFD[T <: Tag](tag: T, count: Int, data: ByteSeq)
  extends IfdAttribute(tag, Type.Byte, count, data) {
  type V = ByteSeq
  override val value = data
}

class ShortIFD[T <: Tag](tag: T, count: Int, data: ByteSeq, order: ByteSeq)
  extends IfdAttribute(tag, Type.Short, count, data) {
  type V = Seq[Int]
  val value: Seq[Int] = for (i <- 0 until count) yield data.toShort(i * tagType.size, order)
}

class LongIFD[T <: Tag](tag: T, count: Int, data: ByteSeq, order: ByteSeq)
  extends IfdAttribute(tag, Type.Long, count, data) {
  type V = Seq[Long]
  val value: Seq[Long] = for (i <- 0 until count) yield data.toLong(i * tagType.size, order)
}

class RationalIFD[T <: Tag](tag: T, count: Int, data: ByteSeq, order: ByteSeq)
  extends IfdAttribute(tag, Type.Rational, count, data) {
  type V = Seq[Rational]
  val value: Seq[Rational] = for (i <- 0 until count) yield data.toRational(i * tagType.size, order)
}

class UndefinedIFD[T <: Tag](tag: T, count: Int, data: ByteSeq)
  extends IfdAttribute(tag, Type.Ascii, count, data) {
  type V = ByteSeq
  val value = data
}

class SignedShortIFD[T <: Tag](tag: Tag, count: Int, data: ByteSeq, order: ByteSeq)
  extends IfdAttribute(tag, Type.Short, count, data) {
  type V = Seq[Short]
  val value: Seq[Short] = for (i <- 0 until count) yield data.toSignedShort(i * tagType.size, order)
}

class SignedLongIFD[T <: Tag](tag: T, count: Int, data: ByteSeq, order: ByteSeq)
  extends IfdAttribute(tag, Type.SLong, count, data) {
  type V = Seq[Int]
  val value: Seq[Int] = for (i <- 0 until count) yield data.toSignedLong(i * tagType.size, order)
}

class SignedRationalIFD[T <: Tag](tag: T, count: Int, data: ByteSeq, order: ByteSeq)
  extends IfdAttribute(tag, Type.SRational, count, data) {
  type V = Seq[SignedRational]
  val value: Seq[SignedRational] = for (i <- 0 until count) yield data.toSignedRational(i * tagType.size, order)
}

class UnknownIFD[T <: Tag](tag: T, tagType: Type, count: Int, data: ByteSeq) extends IfdAttribute(tag, tagType, count, data) {
  type V = ByteSeq
  override val value = data
}
