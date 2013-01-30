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

import org.scalatest.FunSuite

import java.io.FileNotFoundException

class JpegMetaDataTest extends FunSuite {

  test("Read JPEG meta-data") {
    val metadata = load("image.jpg")
    assert(metadata.exif != None, "Exif tag found")
  }

  expect(8, "Orientation") {
    val metadata = load("image-vertical.jpg")
    metadata.exif match {
      case Some(exif) => {
        exif.orientation 
      }
      case None => -1
    }
  }
    
  test("GPS Data") {
    val metadata = load("image-gps.jpg")
    metadata.exif match {
      case Some(exif) => exif.attr(GpsIfd.GPSLatitude) match {
        case Some(attr: RationalIFD[_]) => 
          assert(attr.value.length === 3)
          assert(attr.value(0) === Rational(48, 1))
        case None => fail("GPSLatitude not found")
      }
      case None => fail("Exif segment not found")
    }
  }
  
  expect(64, "All Attributes found") {
    val metadata = load("image-gps.jpg")
    metadata.exif match {
      case Some(exif) => exif.allAttrs.length
      case None => fail("Exif segment not found")
    }
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
