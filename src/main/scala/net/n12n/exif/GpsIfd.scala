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

/**
 * GPS IFD.
 * @author niklas
 *
 */
class GpsIfd(exif: ExifSegment, offset: Int) extends Ifd(exif, offset, "GPS IFD") {
  override type TagType = GpsTag[_]
  override val Tags = GpsIfd.Tags
}

trait GpsTag[T] extends TypedTag[T]

object GpsIfd {
  
  val GPSVersionID = new ByteTag(0, "GPSVersionID") with GpsTag[ByteSeq]
  val GPSLatitudeRef = new AsciiTag(1, "GPSLatitudeRef") with GpsTag[String]
  val GPSLatitude = new RationalListTag(2, "GPSLatitude") with GpsTag[List[Rational]]
  val GPSLongitudeRef = new AsciiTag(3, "GPSLongitudeRef") with GpsTag[String]
  val GPSLongitude = new RationalListTag(4, "GPSLongitude") with GpsTag[List[Rational]]
  val GPSAltitudeRef = new ByteTag(5, "GPSAltitudeRef") with GpsTag[ByteSeq]
  val GPSAltitude = new RationalTag(6, "GPSAltitude") with GpsTag[Rational]
  val GPSTimeStamp = new RationalListTag(7, "GPSTimeStamp") with GpsTag[List[Rational]]
  val GPSSatellites = new AsciiTag(8, "GPSSatellites") with GpsTag[String]
  val GPSStatus = new AsciiTag(9, "GPSStatus") with GpsTag[String]
  val GPSMeasureMode = new AsciiTag(10, "GPSMeasureMode") with GpsTag[String]
  val GPSDOP = new RationalTag(11, "GPSDOP") with GpsTag[Rational]
  val GPSSpeedRef = new AsciiTag(12, "GPSSpeedRef") with GpsTag[String]
  val GPSSpeed = new RationalTag(13, "GPSSpeed") with GpsTag[Rational]
  val GPSTrackRef = new AsciiTag(14, "GPSTrackRef") with GpsTag[String]
  val GPSTrack = new RationalTag(15, "GPSTrack") with GpsTag[Rational]
  val GPSImgDirectionRef = new AsciiTag(16, "GPSImgDirectionRef") with GpsTag[String]
  val GPSImgDirection = new RationalTag(17, "GPSImgDirection") with GpsTag[Rational]
  val GPSMapDatum = new AsciiTag(18, "GPSMapDatum") with GpsTag[String]
  val GPSDestLatitudeRef = new AsciiTag(19, "GPSDestLatitudeRef") with GpsTag[String]
  val GPSDestLatitude = new RationalListTag(20, "GPSDestLatitude") with GpsTag[List[Rational]]
  val GPSDestLongitudeRef = new AsciiTag(21, "GPSDestLongitudeRef") with GpsTag[String]
  val GPSDestLongitude = new RationalListTag(22, "GPSDestLongitude") with GpsTag[List[Rational]]
  val GPSDestBearingRef = new AsciiTag(23, "GPSDestBearingRef") with GpsTag[String]
  val GPSDestBearing = new RationalListTag(24, "GPSDestBearing") with GpsTag[List[Rational]]
  val GPSDestDistanceRef = new AsciiTag(25, "GPSDestDistanceRef") with GpsTag[String]
  val GPSDestDistance = new RationalTag(26, "GPSDestDistance") with GpsTag[Rational]
  val GPSProcessingMethod = new UndefinedTag(27, "GPSProcessingMethod") with GpsTag[Undefined]
  val GPSAreaInformation = new UndefinedTag(28, "GPSAreaInformation") with GpsTag[Undefined]
  val GPSDateStamp = new AsciiTag(29, "GPSDateStamp") with GpsTag[String]
  val GPSDifferential = new ShortTag(30, "GPSDifferential") with GpsTag[Int]
  val Tags = Set[GpsTag[_]](GPSVersionID,
    GPSLatitudeRef,
    GPSLatitude,
    GPSLongitudeRef,
    GPSLongitude,
    GPSAltitudeRef,
    GPSAltitude,
    GPSTimeStamp,
    GPSSatellites,
    GPSStatus,
    GPSMeasureMode,
    GPSDOP,
    GPSSpeedRef,
    GPSSpeed,
    GPSTrackRef,
    GPSTrack,
    GPSImgDirectionRef,
    GPSImgDirection,
    GPSMapDatum,
    GPSDestLatitudeRef,
    GPSDestLatitude,
    GPSDestLongitudeRef,
    GPSDestLongitude,
    GPSDestBearingRef,
    GPSDestBearing,
    GPSDestDistanceRef,
    GPSDestDistance,
    GPSProcessingMethod,
    GPSAreaInformation,
    GPSDateStamp,
    GPSDifferential)
}