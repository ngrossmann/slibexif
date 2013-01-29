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
class GpsIfd(exif: ExifSegment, offset: Int) extends Ifd(exif, offset, GpsIfd.marker2tag)

case class GpsTag(marker: Int, name: String) extends Tag

object GpsIfd extends IfdObjectBase[GpsTag] {
  override val tag = (marker: Int, name: String) => GpsTag(marker, name)
  
  val GPSVersionID = GpsTag(0, "GPSVersionID")
  val GPSLatitudeRef = GpsTag(1, "GPSLatitudeRef")
  val GPSLatitude = GpsTag(2, "GPSLatitude")
  val GPSLongitudeRef = GpsTag(3, "GPSLongitudeRef")
  val GPSLongitude = GpsTag(4, "GPSLongitude")
  val GPSAltitudeRef = GpsTag(5, "GPSAltitudeRef")
  val GPSAltitude = GpsTag(6, "GPSAltitude")
  val GPSTimeStamp = GpsTag(7, "GPSTimeStamp")
  val GPSSatellites = GpsTag(8, "GPSSatellites")
  val GPSStatus = GpsTag(9, "GPSStatus")
  val GPSMeasureMode = GpsTag(10, "GPSMeasureMode")
  val GPSDOP = GpsTag(11, "GPSDOP")
  val GPSSpeedRef = GpsTag(12, "GPSSpeedRef")
  val GPSSpeed = GpsTag(13, "GPSSpeed")
  val GPSTrackRef = GpsTag(14, "GPSTrackRef")
  val GPSTrack = GpsTag(15, "GPSTrack")
  val GPSImgDirectionRef = GpsTag(16, "GPSImgDirectionRef")
  val GPSImgDirection = GpsTag(17, "GPSImgDirection")
  val GPSMapDatum = GpsTag(18, "GPSMapDatum")
  val GPSDestLatitudeRef = GpsTag(19, "GPSDestLatitudeRef")
  val GPSDestLatitude = GpsTag(20, "GPSDestLatitude")
  val GPSDestLongitudeRef = GpsTag(21, "GPSDestLongitudeRef")
  val GPSDestLongitude = GpsTag(22, "GPSDestLongitude")
  val GPSDestBearingRef = GpsTag(23, "GPSDestBearingRef")
  val GPSDestBearing = GpsTag(24, "GPSDestBearing")
  val GPSDestDistanceRef = GpsTag(25, "GPSDestDistanceRef")
  val GPSDestDistance = GpsTag(26, "GPSDestDistance")
  val GPSProcessingMethod = GpsTag(27, "GPSProcessingMethod")
  val GPSAreaInformation = GpsTag(28, "GPSAreaInformation")
  val GPSDateStamp = GpsTag(29, "GPSDateStamp")
  val GPSDifferential = GpsTag(30, "GPSDifferential")
  override val Tags = Set(GPSVersionID,
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