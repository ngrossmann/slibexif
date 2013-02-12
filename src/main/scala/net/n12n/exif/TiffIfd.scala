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
 * IFD defined by TIFF standard.
 *  
 * @param exif The Exif segment containing this IFD.
 * @param offset Start of this IFD relative to the [[net.n12n.exif.ExifSegment#tiffOffset]].
 */
class TiffIfd(exif: ExifSegment, offset: Int, name: String) extends Ifd(exif, offset, name) {
  override type T = TiffTag
  override val Tags = TiffIfd.Tags
  override protected def createTag(marker: Int, tagType: Type, count: Int) = {
    tagType match {
      case Type.Ascii => new T(marker, "Unknown") with AsciiTag
      case Type.Byte => new T(marker, "Unknown") with ByteTag
      case Type.Undefined => new T(marker, "Unknown") with UndefinedTag
      case Type.Long if (count == 1) => new T(marker, "Unknown") with LongTag
      case Type.Long => new T(marker, "Unknown") with LongListTag
      case Type.Short if (count == 1) => new T(marker, "Unkown") with ShortTag
      case Type.Short => new T(marker, "Unknown") with ShortListTag
      case _ => throw new IllegalArgumentException("Tag %x of type %s".format(marker, tagType))
    } 
  }
}

class TiffTag(marker: Int, name: String) extends Tag(marker, name)

object TiffIfd {
  val ImageWidth = new TiffTag(256, "ImageWidth") with NumericTag
  val ImageLength = new TiffTag(257, "ImageLength") with NumericTag
  val BitsPerSample = new TiffTag(258, "BitsPerSample") with ShortListTag
  val Compression = new TiffTag(259, "Compression") with ShortTag
  val PhotometricInterpretation = new TiffTag(262, "PhotometricInterpretation") with ShortTag
  val Orientation = new TiffTag(274, "Orientation") with ShortTag
  val SamplesPerPixel = new TiffTag(277, "SamplesPerPixel") with ShortTag
  val PlanarConfiguration = new TiffTag(284, "PlanarConfiguration") with ShortTag
  val YCbCrSubSampling = new TiffTag(530, "YCbCrSubSampling") with ShortListTag
  val YCbCrPositioning = new TiffTag(531, "YCbCrPositioning") with ShortTag
  val ExifIfdPointer = new TiffTag(0x8769, "ExifIfdPointer") with LongTag
  val GpsInfoIfdPointer = new TiffTag(0x8825, "GpsInfoIfdPointer") with LongTag
  val XResolution = new TiffTag(282, "XResolution") with RationalTag
  val YResolution = new TiffTag(283, "YResolution") with RationalTag
  /** 2 = inches, 3 = centimeters, others = reserved. */
  val ResolutionUnit = new TiffTag(296, "ResolutionUnit") with ShortTag
  val StripOffsets = new TiffTag(273, "StripOffsets") with NumericListTag
  val RowsPerStrip = new TiffTag(278, "RowsPerStrip") with NumericTag
  val StripByteCounts = new TiffTag(279, "StripByteCounts")  with NumericListTag
  val JPEGInterchangeFormat = new TiffTag(513, "JPEGInterchangeFormat") with LongTag
  val JPEGInterchangeFormatLength = new TiffTag(514, "JPEGInterchangeFormatLength") with LongTag
  val TransferFunction = new TiffTag(301, "TransferFunction") with ShortListTag
  val WhitePoint = new TiffTag(318, "WhitePoint") with RationalListTag
  val PrimaryChromaticities = new TiffTag(319, "PrimaryChromaticities") with RationalListTag
  val YCbCrCoefficients = new TiffTag(529, "YCbCrCoefficients") with RationalListTag
  val ReferenceBlackWhite = new TiffTag(532, "ReferenceBlackWhite") with RationalListTag
  val DateTime = new TiffTag(306, "DateTime") with AsciiTag
  val ImageDescription = new TiffTag(270, "ImageDescription") with AsciiTag
  val Make = new TiffTag(271, "Make") with AsciiTag
  val Model = new TiffTag(272, "Model") with AsciiTag
  val Software = new TiffTag(305, "Software") with AsciiTag
  val Artist = new TiffTag(315, "Artist") with AsciiTag
  val Copyright = new TiffTag(33432, "Copyright") with AsciiTag
  
  val Tags = Set[TiffTag with TypedTag[_]](ImageWidth, ImageLength, BitsPerSample, Compression,
      PhotometricInterpretation,
    Orientation, SamplesPerPixel, PlanarConfiguration, YCbCrSubSampling, YCbCrPositioning,
    ExifIfdPointer, GpsInfoIfdPointer, XResolution, YResolution, ResolutionUnit, StripOffsets, 
    RowsPerStrip, StripByteCounts, JPEGInterchangeFormat, JPEGInterchangeFormatLength, 
    TransferFunction, WhitePoint, PrimaryChromaticities, YCbCrCoefficients, ReferenceBlackWhite, 
    DateTime, ImageDescription, Make, Model, Software, Artist, Copyright)  
}
