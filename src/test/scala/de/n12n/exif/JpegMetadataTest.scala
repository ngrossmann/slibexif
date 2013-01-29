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
  
  test("toString") {
    val metadata = load("image.jpg")
    println(metadata)
  }
  
  test("GPS Data") {
    println(load("image-gps.jpg"))
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
