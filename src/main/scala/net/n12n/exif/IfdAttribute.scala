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
   * @param exif the Exif segment data.
   * @param tagOffset start of the tag to be created relative to the tiffOffset.
   * @param tiffOffset start of TIFF header in EXIF data.
   * @param bytes2Tag look-up function from tuple (marker, type, count) to tag.
   */
  def apply(exif: ByteSeq, tagOffset: Int, tiffOffset: Int, order: ByteOrder,
      bytes2Tag: (Int, Type, Int) => TypedTag[_]): IfdAttribute = {
    val start = tagOffset + tiffOffset
    val typeId = exif.toShort(start + 2, order)
    val count = exif.toSignedLong(start + 4, order)
    val tag = bytes2Tag(exif.toShort(start, order), Type.value(typeId), count)
    val size = Type.size(typeId) * count
    val dataOffset = if (size > 4)
        exif.toSignedLong(start + 8, order) + tiffOffset
      else
        start + 8
    new IfdAttribute(tag, typeId, count, exif.slice(dataOffset, dataOffset + size), order)
  }
}

class IfdAttribute(val tag: TypedTag[_], val typeId: Int, val count: Int, val data: ByteSeq, order: ByteOrder) {
  /** The Scala type to which this attribute maps. */
  // FIXME require(tagType.size * count == data.length)

  def value = tag.value(this, order)

  override def toString(): String = {
    val sval = value match {
      case seq: Seq[_] => "[" + seq.mkString(" ") + "]"
      case obj => obj.toString()
    }
    "%s(%s, %s)".format(getClass().getSimpleName(), tag, sval)
  }
}

//class GenericIfdAttribute[T](tag: TypedTag[T], tagType: Type, count: Int, data: ByteSeq,
//    order: ByteOrder)
//  extends IfdAttribute(tag, tagType, count, data) {
//  override type V = T
//  override lazy val value: V = tag.value(this, order)
//}

//class GenericIfdListAttribute[T](tag: TypedTag[T], tagType: Type, count: Int, data: ByteSeq, 
//    order: ByteOrder) 
//  extends IfdAttribute(tag, tagType, count, data) {
//  override type V = List[T]
//  override val value: V = (for (i <- 0 until count) 
//    yield tagType.toScala(data,i * tagType.size, order)).toList
//}
