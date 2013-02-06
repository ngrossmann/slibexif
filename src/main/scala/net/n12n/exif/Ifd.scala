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
 * Image-File-Directory (IFD) structure.
 * 
 * @param exif The Exif segment containing this IFD.
 * @param offset Start of this IFD relative to the [[net.n12n.exif.ExifSegment#tiffOffset]].
 */
abstract class Ifd(exif: ExifSegment, offset: Int, val name: String) {
  type T <: Tag
  /** Set of tags. */
  val Tags: Set[T]
  val count = exif.data.toShort(exif.tiffOffset + offset, exif.byteOrder)
  lazy val tags: Seq[IfdAttribute[T]] = for (i <- 0 until count) yield 
    IfdAttribute(exif.data, offset + 2 + i * IfdAttribute.Length, exif.tiffOffset, exif.byteOrder,
        bytes2tag)
  /**
   * Offset to next IDF relative to the Exif's TiffHeader.
   */
  lazy val nextIfd = exif.data.toSignedLong(exif.tiffOffset + offset + 2 + count * IfdAttribute.Length,
      exif.byteOrder)
  
  /**
   * Find attribute in IFD.
   * @param tag Attribute tag.
   * @return {{Some(attr)}} or {{None}}.
   */
  def findAttr(tag: T): Option[IfdAttribute[T]] = tags.find(_.tag == tag)
  
  /**
   * Get attribute by tag.
   * @param tag Attribute's tag.
   * @return Attribute.
   * @throws AttributeNotFoundException If the attribute was not found in this IFD.
   */
  def attr(tag: T): IfdAttribute[T] = findAttr(tag).getOrElse(throw AttributeNotFoundException(tag.name))
  
  /**
   * Get attribute value by tag.
   * @param tag Tag
   * @return Attribute value
   * @throws AttributeNotFoundException If the attribute was not found in this IFD.
   */
  def value[V](tag: TypedTag[V]): V = {
    val ifdAttr: IfdAttribute[_] = tags.find(_.tag == tag).getOrElse(
        throw AttributeNotFoundException(tag.name))
    tag.value(ifdAttr, exif.byteOrder)
  }

  protected def bytes2tag(id: Int): T = 
    Tags.find(_.marker == id).getOrElse(createTag(id))
  
  protected def createTag(id: Int): T
  
  override def toString() = "IFD(%x, %x)\n%s".format(count, nextIfd, tags.mkString("  \n"))
}
