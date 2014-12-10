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

import org.scalatest.FunSuite

import java.io.FileNotFoundException

class JpegMetaDataTest extends FunSuite {

  test("Read JPEG meta-data") {
    val metadata = load("image.jpg")
    assert(metadata.exif != None, "Exif tag found")
    assert(metadata.comments.length === 1, "Comment found")
  }
  
  expectResult(Orientation.LeftBottom, "Orientation") {
    val metadata = load("image-vertical.jpg")
    metadata.exif.get.orientation
  }
  
  expectResult(Rational(48, 1), "GPS Data") {
    val metadata = load("image-gps.jpg")
    val lat = for {
      exif <- metadata.exif
      gpsIfd <- exif.gpsIfd
      value <- gpsIfd.findValue(GpsIfd.GPSLatitude)
    } yield value(0)
    lat.get
  }
  
  expectResult(65, "All Attributes found") {
    val metadata = load("image-gps.jpg")
    metadata.exif.get.allAttrs.length
  }

  private def load(file: String) = {
    try {
      new JpegMetaData(classOf[JpegMetaData].getResourceAsStream(file))
    } catch {
      case e: NullPointerException =>
        System.err.println(file + " not found")
        throw new FileNotFoundException(file)
      case e: Exception =>
        System.err.println("Unexpected Exception " + e)
        throw e
    }
  }
}
