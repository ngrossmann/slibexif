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

object Segment {
  case class Marker(marker: Int, name: String) extends Tag
  val JfifMarker = Marker(0xffe0, "JFIF")
  val App1Marker = Marker(0xffe1, "APP1")
  val App2Marker = Marker(0xffe2, "APP2")
  val DqtMarker = Marker(0xffdb, "DQT")
  val DhtMarker = Marker(0xffc4, "DHT")
  val DriMarker = Marker(0xffdd, "DRI")
  val SofMarker = Marker(0xffc0, "SOF")
  val SosMarker = Marker(0xffda, "SOS")
  val EoiMarker = Marker(0xffd9, "EOI")
  val ComMarker = Marker(0xfffe, "Comment")
  val Exif = ByteSeq("Exif")
  
  val Markers = Set(JfifMarker, App1Marker, App2Marker, DqtMarker, DhtMarker, DriMarker, 
      SofMarker, SosMarker, EoiMarker, ComMarker)
  
  def create(marker: ByteSeq, in: ByteStream): Segment = {
    val markerValue = marker.toShort(0, ByteSeq.BigEndian)
    val tag = Markers.find(_.marker == markerValue) match {
      case Some(t) => t
      case None => Marker(markerValue, "Unknown Marker")
    }
    val length = (in.next() << 8) + in.next()
    val data = ByteSeq(length - 2, in)
    
    if (tag == Segment.App1Marker && data.slice(0, Exif.length) == Exif)
      new ExifSegment(length, data)
    else if (marker == ComMarker)
      new ComSegment(ComMarker, length, data)
    else {
      new Segment(tag, length, data)
    }
  }
  
  def bytes2Num(a: Array[Int], offset: Int): Int = {
    if (offset >= a.length)
      return 0
	else
	  a(offset) << (offset * 8) + bytes2Num(a, offset + 1)
  }
}

/**
 * A segment is a byte sequence starting with a two byte marker, a two byte length field
 * followed by data.
 * The length is defined as the length of the data plus 2 bytes (the length field itself)
 * but without the marker.
 */
class Segment(val marker: Tag, val length: Int, val data: ByteSeq) {
      
  override def toString = {
    "Segment(marker: %1$s, lenghth: %2$x)".format(marker, length)
  }
}

class ComSegment(marker: Tag, length: Int, data: ByteSeq) 
	extends Segment(marker, length, data) {
  val comment = data.zstring(0)
  
  override def toString:String = {
    return "Comment(%1$s)".format(comment)
  }
}
