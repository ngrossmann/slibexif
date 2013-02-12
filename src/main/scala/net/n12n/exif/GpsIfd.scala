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

/**
 * @author niklas
 *
 */
class GpsIfd(exif: ExifSegment, offset: Int) extends Ifd(exif, offset, "GPS IFD") {
  override type T = GpsTag
  override val Tags = GpsIfd.Tags
  override protected def createTag(marker: Int) = new GpsTag(marker, "Unknown GPS Tag")
}

class GpsTag(marker: Int, name: String) extends Tag(marker, name)

object GpsIfd {
  
  val GPSVersionID = new GpsTag(0, "GPSVersionID") with ByteTag
  val GPSLatitudeRef = new GpsTag(1, "GPSLatitudeRef") with AsciiTag
  val GPSLatitude = new GpsTag(2, "GPSLatitude") with RationalListTag
  val GPSLongitudeRef = new GpsTag(3, "GPSLongitudeRef") with AsciiTag
  val GPSLongitude = new GpsTag(4, "GPSLongitude") with RationalListTag
  val GPSAltitudeRef = new GpsTag(5, "GPSAltitudeRef") with ByteTag
  val GPSAltitude = new GpsTag(6, "GPSAltitude") with RationalTag
  val GPSTimeStamp = new GpsTag(7, "GPSTimeStamp") with RationalListTag
  val GPSSatellites = new GpsTag(8, "GPSSatellites") with AsciiTag
  val GPSStatus = new GpsTag(9, "GPSStatus") with AsciiTag
  val GPSMeasureMode = new GpsTag(10, "GPSMeasureMode") with AsciiTag
  val GPSDOP = new GpsTag(11, "GPSDOP") with RationalTag
  val GPSSpeedRef = new GpsTag(12, "GPSSpeedRef") with AsciiTag
  val GPSSpeed = new GpsTag(13, "GPSSpeed") with RationalTag
  val GPSTrackRef = new GpsTag(14, "GPSTrackRef") with AsciiTag
  val GPSTrack = new GpsTag(15, "GPSTrack") with RationalTag
  val GPSImgDirectionRef = new GpsTag(16, "GPSImgDirectionRef") with AsciiTag
  val GPSImgDirection = new GpsTag(17, "GPSImgDirection") with RationalTag
  val GPSMapDatum = new GpsTag(18, "GPSMapDatum") with AsciiTag
  val GPSDestLatitudeRef = new GpsTag(19, "GPSDestLatitudeRef") with AsciiTag
  val GPSDestLatitude = new GpsTag(20, "GPSDestLatitude") with RationalListTag
  val GPSDestLongitudeRef = new GpsTag(21, "GPSDestLongitudeRef") with AsciiTag
  val GPSDestLongitude = new GpsTag(22, "GPSDestLongitude") with RationalListTag
  val GPSDestBearingRef = new GpsTag(23, "GPSDestBearingRef") with AsciiTag
  val GPSDestBearing = new GpsTag(24, "GPSDestBearing") with RationalListTag
  val GPSDestDistanceRef = new GpsTag(25, "GPSDestDistanceRef") with AsciiTag
  val GPSDestDistance = new GpsTag(26, "GPSDestDistance") with RationalTag
  val GPSProcessingMethod = new GpsTag(27, "GPSProcessingMethod") with UndefinedTag
  val GPSAreaInformation = new GpsTag(28, "GPSAreaInformation") with UndefinedTag
  val GPSDateStamp = new GpsTag(29, "GPSDateStamp") with AsciiTag
  val GPSDifferential = new GpsTag(30, "GPSDifferential") with ShortTag
  val Tags = Set[GpsTag](GPSVersionID,
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