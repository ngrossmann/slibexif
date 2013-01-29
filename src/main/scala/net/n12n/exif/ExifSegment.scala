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

object ExifSegment {
  val DefaultOrientation: Short = 1
}

/**
 * Create EXIF segment from byte sequence.
 * 
 * @param marker segment marker, see [[net.n12n.exif.Segment]].
 * @param length segment length, see [[net.n12n.exif.Segment]].
 * @param data segment data.
 */
class ExifSegment(length: Int, data: ByteSeq, offset: Int = 0)
  extends Segment(Segment.App1Marker, length, data) { 
  val tiffOffset = offset + 6
  private val tiffMarker = 42
  val name = data.zstring(offset);
  val byteOrder = data.slice(tiffOffset, tiffOffset + 2)
  if (data.toSignedShort(tiffOffset + 2, byteOrder) != tiffMarker)
    throw new IllegalArgumentException("Not an EXIF segment TIFF marker not found(%d)".format(
        data.toSignedShort(tiffOffset + 2, byteOrder)))
  private val ifdOffset = data.toSignedLong(tiffOffset + 4, byteOrder)
  val ifd0 = new TiffIfd(this, ifdOffset)
  val ifd1: Option[TiffIfd] = if (ifd0.nextIfd != 0) Some(new TiffIfd(this, ifd0.nextIfd)) else None
  val exifIfd: Option[ExifIfd] = ifd0.tags.find(_.tag == TiffIfd.ExifIfdPointer) match {
    case Some(pointer: LongIFD[_]) => Some(new ExifIfd(this, pointer.value.head.toInt))
    case None => None
  }
  val gpsIfd: Option[GpsIfd] = ifd0.tags.find(_.tag == TiffIfd.GpsInfoIfdPointer) match {
    case Some(pointer: LongIFD[_]) => Some(new GpsIfd(this, pointer.value.head.toInt))
    case None => None
  }
  /**
   * Image orientation.
   * {{{
   * Default = 1
   * 1 = The 0th row is at the visual top of the image, and the 0th column is the visual left-hand side.
   * 2 = The 0th row is at the visual top of the image, and the 0th column is the visual right-hand side.
   * 3 = The 0th row is at the visual bottom of the image, and the 0th column is the visual right-hand side.
   * 4 = The 0th row is at the visual bottom of the image, and the 0th column is the visual left-hand side.
   * 5 = The 0th row is the visual left-hand side of the image, and the 0th column is the visual top.
   * 6 = The 0th row is the visual right-hand side of the image, and the 0th column is the visual top.
   * 7 = The 0th row is the visual right-hand side of the image, and the 0th column is the visual bottom.
   * 8 = The 0th row is the visual left-hand side of the image, and the 0th column is the visual bottom.
   * Other = reserved
   * }}}
   */
  val orientation = ifd0.tags.find(_.tag == TiffIfd.Orientation) match {
    case Some(ifd) => ifd.data.toShort(0, byteOrder)
    case _ => ExifSegment.DefaultOrientation
  }
    
  override def toString() = {
    val opt2string = (opt: Option[_]) => opt match {
      case Some(obj) => obj.toString()
      case None => "not set"
    }
    "ExifSegment(%s, %x bytes)\nIFD0: %s\nIFD1\n%s\nExifIFD: %s\nGPS IFD: %s".format(
        name, length, ifd0, opt2string(ifd1), opt2string(exifIfd), opt2string(gpsIfd)) 
  }
}
