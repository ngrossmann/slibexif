/* 
 * slibexif - Scala library to parse JPEG EXIF data.
 * Copyright (C) Niklas Grossmann
 * 
 * This file is part of libexif.
 *
 * slibexif is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * slibexif is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with libexif.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.n12n.exif

import java.io.InputStream
import scala.io.BufferedSource
import java.lang.{IllegalArgumentException, IllegalStateException}
import java.io.FileInputStream

/**
 * Read read JPEG meta-data.
 *
 * Example: List all attributes
 * {{{
 *     JpegMetaData("image.jpg").exif.foreach(_.ifds.flatMap(_.attributes).foreach(
 *       attr => println(s"\${attr.tag.name}: \${attr.value}")))
 * }}}
 */
object JpegMetaData {
  /**
   * Read image from file.
   * @param file filename.
   */
  def apply(file: String): JpegMetaData = new JpegMetaData(new FileInputStream(file))
}

/**
 * Read read JPEG meta-data.
 *
 * @constructor Read JPEG from byte stream.
 * 
 * @param data JPEG data stream.
 */
class JpegMetaData(data: InputStream) {
  /** Byte sequence marking Start Of Image. */
  val SoiMarker = ByteSeq(0xff, 0xd8)
  /** Byte sequence marking Start of Scan header */
  val SosMarker = ByteSeq(0xff, 0xda)

  private val in = new ByteStream(data)
  private val soi = ByteSeq(2, in)

  if (SoiMarker != soi) throw new IllegalArgumentException("Not a JPEG image, expected " + SoiMarker +
    " found " + soi)
  /** List of meta-data segments (exif and comment). */
  val segments = parseSegments(in)
  /** Direct access to comment segments */
  val comments: List[ComSegment] = segments.filter(_.marker == Segment.ComMarker).
    map(_.asInstanceOf[ComSegment])
  /**  Direct access to exif segment. */
  val exif: Option[ExifSegment] = 
    segments.find(_.isInstanceOf[ExifSegment]).map(_.asInstanceOf[ExifSegment])
  
  /**
   * Total size of the image.
   */
  lazy val size = segments.map(_.length).sum + SoiMarker.length + SosMarker.length
  
  /**
   * JPEG meta-data as string.
   * @return All segments contained in meta-data, one per line
   */
  override def toString = {
    segments.mkString("\n")
  }
  
  private def parseSegments(in: ByteStream): List[Segment] = {
    val marker = ByteSeq(2, in)
    if (marker == SosMarker) Nil
    else Segment.create(marker, in) :: parseSegments(in) 
  }
}
