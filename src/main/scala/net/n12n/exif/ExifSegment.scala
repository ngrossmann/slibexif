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
  /** Marker in Tiff header. */
  private[ExifSegment] val TiffMarker = 42
  private[ExifSegment] val LittleEndianMarker = ByteSeq("II")
  private[ExifSegment] val BigEndianMarker = ByteSeq("MM")
}

/**
 * Exif segment.
 * An Exif segment contains at least one IFD (Image File Directory), the first IFD, called 
 * ''0thIFD'' is mandatory all others are optional.
 * 
 * To see which attributes are defined in which IFD check the corresponding companion objects
 * [[net.n12n.exif.TiffIfd]], [[net.n12n.exif.ExifIfd]] and [[net.n12n.exif.GpsIfd]].
 *  
 * @param marker segment marker, see [[net.n12n.exif.Segment]].
 * @param length segment length, see [[net.n12n.exif.Segment]].
 * @param data segment data.
 */
class ExifSegment(length: Int, data: ByteSeq, offset: Int = 0) extends Segment(Segment.App1Marker, length, data) {
  import ExifSegment._
  /** Offset of TIFF segment. */
  private[exif] val tiffOffset = offset + 6
  private val name = data.zstring(offset)
  /** Byte order as given in TIFF segment. */
  val byteOrder = if (data.slice(tiffOffset, tiffOffset + 2) == LittleEndianMarker) 
    ByteOrder.LittleEndian else ByteOrder.BigEndian 
  if (data.toSignedShort(tiffOffset + 2, byteOrder) != TiffMarker)
    throw new IllegalArgumentException("Not an EXIF segment TIFF marker not found(%d)".format(
        data.toSignedShort(tiffOffset + 2, byteOrder)))
  private val ifdOffset = data.toSignedLong(tiffOffset + 4, byteOrder)
  /** The 0th IFD, always present. */
  val ifd0: TiffIfd = new TiffIfd(this, ifdOffset, "IFD0")
  /** Optional 1st IFD. */
  val ifd1: Option[TiffIfd] = if (ifd0.nextIfd != 0) Some(new TiffIfd(this, ifd0.nextIfd, "IFD1")) else None
  /** Optional Exif IFD. */
  val exifIfd: Option[ExifIfd] = ifd0.findValue(TiffIfd.ExifIfdPointer).
    map((pointer) => new ExifIfd(this, pointer.toInt))
  /** Optional GPS IFD. */
  val gpsIfd: Option[GpsIfd] = ifd0.findValue(TiffIfd.GpsInfoIfdPointer).
    map(pointer => new GpsIfd(this, pointer.toInt))
  
  /** List of all IFDs in this Exif segment. */
  lazy val ifds = ifd0 :: ifd1.toList ::: exifIfd.toList ::: gpsIfd.toList
  
  /** List of all attributes of all IFDs of this segment. */
  lazy val allAttrs: List[IfdAttribute] = ifds.flatMap(_.attributes)

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
   * In case the EXIF segment does not contain a [net.n12n.exif.TiffIfd.Orientation] attribute or
   * the value of the attribute is not in the range of 1 to 8 this field is set to 
   * [[net.n12n.exif.Orientation.TopLeft]].
   */
  val orientation: Orientation.Orientation = try {
    Orientation(ifd0.findValue(TiffIfd.Orientation).getOrElse(ExifSegment.DefaultOrientation))
  } catch {
    case e: java.util.NoSuchElementException => Orientation(ExifSegment.DefaultOrientation)
  }
  
  /**
   * Find Tiff attribute in 0th or 1st IFD.
   * @param tag Attribute tag.
   * @return Option containing the attribute or ``None``.
   */
  def findAttr(tag: TiffTag[_]): Option[IfdAttribute] =
    (ifd0.findAttr(tag) ++ ifd1.flatMap(_.findAttr(tag))).headOption

  /**
   * Get value of attribute in 0th and 1st IFD.
   * @param tag Tiff tag.
   * @tparam T tag type.
   * @return value of tag wrapped in option.
   */
  def value[T](tag: TiffTag[T]): Option[T] = findAttr(tag).map(tag.value(_, byteOrder))

  /**
   * Find Exif IFD attribute without checking if there is an Exif IFD at all.
   * @param tag Attribute tag.
   * @return Option containing the attribute or {{None}}.
   */
  def findAttr(tag: ExifTag[_]): Option[IfdAttribute] = exifIfd.flatMap(_.findAttr(tag))

  /**
   * Get value of Exif attribute.
   * @param tag Exif tag.
   * @tparam T tag type.
   * @return value of tag wrapped in option.
   */
  def value[T](tag: ExifTag[T]): Option[T] = findAttr(tag).map(tag.value(_, byteOrder))

  /**
   * Find GPS attribute without checking if there is a GPS IFD at all.
   * @param tag Attribute tag.
   * @return Option containing the attribute or {{None}}.
   */
  def findAttr(tag: GpsTag[_]): Option[IfdAttribute] = gpsIfd.flatMap(_.findAttr(tag))

  /**
   * Get value of GPS attribute.
   * @param tag GPS tag.
   * @tparam T tag type.
   * @return value of tag wrapped in option.
   */
  def value[T](tag: GpsTag[T]): Option[T] = findAttr(tag).map(tag.value(_, byteOrder))

  override def toString = {
    val opt2string = (opt: Option[_]) => opt.map(_.toString).getOrElse("not set")
    "ExifSegment(%s, %x bytes)\nIFD0: %s\nIFD1\n%s\nExifIFD: %s\nGPS IFD: %s".format(
        name, length, ifd0, opt2string(ifd1), opt2string(exifIfd), opt2string(gpsIfd)) 
  }
}
