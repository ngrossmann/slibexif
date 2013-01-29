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

/**
 * Image-File-Directory (IFD) structure.
 * 
 * @param exif The Exif segment containing this IFD.
 * @param offset Start of this IFD relative to the [[de.n12n.exif.ExifSegment#tiffOffset]].
 */
class Ifd[T <: IfdAttribute[U], U <: Tag](exif: ExifSegment, offset: Int, bytes2tag: (Int) => U) {
  val count = exif.data.toShort(exif.tiffOffset + offset, exif.byteOrder)
  val tags = for (i <- 0 until count) yield 
    IfdAttribute(exif.data, offset + 2 + i * IfdAttribute.Length, exif.tiffOffset, exif.byteOrder,
        bytes2tag)
  val nextIfd = exif.data.toSignedLong(exif.tiffOffset + offset + 2 + count * IfdAttribute.Length,
      exif.byteOrder)
  
  override def toString() = "IFD(%x, %x)\n%s".format(count, nextIfd, tags.mkString("  \n"))
}