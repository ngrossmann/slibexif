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
 * net.n12n.exif is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with libexif.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.n12n.exif

/**
 * Image orientation.
 */
object Orientation extends Enumeration {
  type Orientation = Value
  /** The 0th row is at the visual top of the image, and the 0th column is the visual left-hand side. */
  val TopLeft = Value(1)
  /** The 0th row is at the visual top of the image, and the 0th column is the visual right-hand side. */
  val TopRight = Value(2)
  /** The 0th row is at the visual bottom of the image, and the 0th column is the visual right-hand side. */
  val BottomRight = Value(3)
  /** The 0th row is at the visual bottom of the image, and the 0th column is the visual left-hand side. */
  val BottomLeft = Value(4)
  /** The 0th row is the visual left-hand side of the image, and the 0th column is the visual top. */
  val LeftTop = Value(5)
  /** The 0th row is the visual right-hand side of the image, and the 0th column is the visual top. */
  val RightTop = Value(6)
  /** The 0th row is the visual right-hand side of the image, and the 0th column is the visual bottom. */
  val RightBottom = Value(7)
  /** The 0th row is the visual left-hand side of the image, and the 0th column is the visual bottom. */
  val LeftBottom = Value(8)
}
