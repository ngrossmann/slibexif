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
object JpegMetaData {
  /**
   * Read image from file.
   * @param file filename.
   */
  def apply(file: String): JpegMetaData = new JpegMetaData(new FileInputStream(file))
}
/**
 * Read JPEG from byte stream.
 * 
 * @param data JPEG data stream.
 */
class JpegMetaData(data: InputStream) {
  val SoiMarker = ByteSeq(0xff, 0xd8)
  val SosMarker = ByteSeq(0xff, 0xda)
  val in = new ByteStream(data)
  val soi = ByteSeq(2, in)
  if (SoiMarker != soi) throw new IllegalArgumentException("Not a JPEG image, expected " + SoiMarker +
    " found " + soi)
  val segments = parseSegments(in)
  /** List of comment segments */
  val comments: List[ComSegment] = segments.filter(_.marker == Segment.ComMarker).
    map(_.asInstanceOf[ComSegment])
  
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
