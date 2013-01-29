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

import java.io.InputStream
import scala.io.BufferedSource
import java.lang.{IllegalArgumentException, IllegalStateException}

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
    
  val comment = segments.find(_.marker == Segment.ComMarker) match {
    case Some(seg: ComSegment) => seg.comment
    case None => ""
  }
  
  val exif: Option[ExifSegment] = segments.find(_.isInstanceOf[ExifSegment]) match {
    case Some(exif: ExifSegment) => Some(exif)
    case _ => None
  }
  
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
