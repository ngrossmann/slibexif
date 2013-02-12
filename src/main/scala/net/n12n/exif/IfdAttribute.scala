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

object IfdAttribute {
  val Length = 12
  /**
   * Create a new IFD tag from EXIF data.
   * @param exif The Exif segment data.
   * @param tagOffset Start of the tag to be created relative to the tiffOffset.
   * @param tiffOffset Start of TIFF header in EXIF data.
   */
  def apply(exif: ByteSeq, tagOffset: Int, tiffOffset: Int, order: ByteOrder,
      bytes2Tag: (Int) => Tag): IfdAttribute = {
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
  
  
  private def create[T <: Tag](namedTag: T, tagType: Type, count: Int, data: ByteSeq, order: ByteOrder): 
    IfdAttribute = {
    tagType match {
      case Type.Ascii => new GenericIfdAttribute(namedTag, Type.Ascii, count, data, order)
      case Type.Byte => new GenericIfdAttribute(namedTag, Type.Byte, count, data, order)
      case Type.Undefined => new GenericIfdAttribute(namedTag, Type.Byte, count, data, order)
      case gt: GenericType[_] if (count == 1) => 
        new GenericIfdAttribute(namedTag, gt, count, data, order)
      case gt: GenericType[_] => new GenericIfdListAttribute(namedTag, gt, count, data, order)
    }
  }
}

abstract class IfdAttribute(val tag: Tag, val tagType: Type, val count: Int, 
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

class GenericIfdAttribute[T](tag: Tag, tagType: GenericType[T], count: Int, data: ByteSeq, 
    order: ByteOrder) 
  extends IfdAttribute(tag, tagType, count, data) {
  override type V = T
  override val value: V = tagType.toScala(data, 0, order)
}

class GenericIfdListAttribute[T](tag: Tag, tagType: GenericType[T], count: Int, data: ByteSeq, 
    order: ByteOrder) 
  extends IfdAttribute(tag, tagType, count, data) {
  override type V = List[T]
  override val value: V = (for (i <- 0 until count) 
    yield tagType.toScala(data,i * tagType.size, order)).toList
}

