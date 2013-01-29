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
 * Base class for IFD companion objects.
 * 
 */
private[exif] abstract class IfdObjectBase[T <: Tag] {
  val Tags: Set[T]
  protected val tag: (Int, String) => T
  
  /**
   * Convert a short marker to a known tag.
   * @param bytes 2 bytes sequence.
   * @return A known [[net.n12n.exif.Tag]] or the original byte sequence.
   */
  val marker2tag = (id: Int) => Tags.find(_.marker == id) match {
    case Some(t) => t
    case None =>  tag(id, "ExifTag")
  }
}