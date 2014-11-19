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
 * An IFD is a container for [[net.n12n.exif.IfdAttribute]]s.
 * 
 * @param exif The Exif segment containing this IFD.
 * @param offset Start of this IFD relative to the ``tiffOffset``.
 */
abstract class Ifd(exif: ExifSegment, offset: Int, val name: String) {
  type TagType <: TypedTag[_]
  /** Set of tags. */
  val Tags: Set[TagType]
  val count = exif.data.toShort(exif.tiffOffset + offset, exif.byteOrder)
  lazy val attributes: Seq[IfdAttribute] = for (i <- 0 until count) yield 
    IfdAttribute(exif.data, offset + 2 + i * IfdAttribute.Length, exif.tiffOffset, exif.byteOrder,
        findTag)
  lazy val tags: Seq[TypedTag[_]] = attributes.map(_.tag)
  
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
  def findAttr(tag: Tag): Option[IfdAttribute] = attributes.find(_.tag.equals(tag))
  
  /**
   * Get attribute by tag.
   * @param tag Attribute's tag.
   * @return Attribute.
   * @throws AttributeNotFoundException If the attribute was not found in this IFD.
   */
  def attr(tag: Tag): IfdAttribute =
    findAttr(tag).getOrElse(throw AttributeNotFoundException(tag.name))
  
  def findValue[V](tag: TypedTag[V]): Option[V] = attributes.find(_.tag.equals(tag)).
  	map(tag.value(_, exif.byteOrder))
  
  /**
   * Get attribute value by tag.
   * @param tag Tag
   * @return Attribute value
   * @throws AttributeNotFoundException If the attribute was not found in this IFD.
   */
  def value[V](tag: TypedTag[V]): V = {
    val ifdAttr: IfdAttribute = attributes.find(_.tag == tag).getOrElse(
        throw AttributeNotFoundException(tag.name))
    tag.value(ifdAttr, exif.byteOrder)
  }

  protected def findTag(id: Int, tagType: Type, count: Int): TypedTag[_] =
    Tags.find(_.marker == id).getOrElse(createTag(id, tagType, count))
  
  protected def createTag(marker: Int, tagType: Type, count: Int): TypedTag[_] = {
    tagType match {
      case Type.Ascii => new AsciiTag(marker, "Unknown")
      case Type.Byte => new ByteTag(marker, "Unknown")
      case Type.Undefined => new UndefinedTag(marker, "Unknown")
      case Type.Long if (count == 1) => new LongTag(marker, "Unknown")
      case Type.Long => new LongListTag(marker, "Unknown")
      case Type.Short if (count == 1) => new ShortTag(marker, "Unknown")
      case Type.Short => new ShortListTag(marker, "Unknown")
      case _ => throw new IllegalArgumentException("Tag %x of type %s".format(marker, tagType))
    }
  }

  override def toString() = "IFD(%x, %x)\n%s".format(count, nextIfd, attributes.mkString("  \n"))
}
