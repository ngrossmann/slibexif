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

trait Tag {
  def marker: Int
  def name: String
}

/**
 * Create a marker tag.
 * @param marker Marking 2-byte sequence, must be in range 0x0 <= marker < 0x10000.
 * @param name Tag name. 
 */
abstract class TagImpl(val marker: Int, val name: String) extends Tag {
  require(marker < 0x10000 && marker >= 0)
  
  /**
   * Two tags are equal if they are of the exactly same type
   * and have the same marker value.
   * @return `true` If tags are equal. 
   */
  override def equals(that: Any): Boolean = {
    if (that != null && that.isInstanceOf[Tag]) {
      val other = that.asInstanceOf[Tag]
      this.getClass() == that.getClass() && other.marker == marker
    } else false
  }
  
  override def toString() = "%s(%04x)".format(name, marker)
}

