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
 * @param offset Start of this IFD relative to the tiffOffset.
 */
class TiffIfd(exif: ExifSegment, offset: Int, name: String) extends Ifd(exif, offset, name) {
  override type TagType = TiffTag[_]
  override val Tags = TiffIfd.Tags
}

trait TiffTag[T] extends TypedTag[T]

object TiffIfd {
  val ImageWidth = new NumericTag(256, "ImageWidth") with TiffTag[Numeric]
  val ImageLength = new NumericTag(257, "ImageLength") with TiffTag[Numeric]
  val BitsPerSample = new ShortListTag(258, "BitsPerSample") with TiffTag[List[Int]]
  val Compression = new ShortTag(259, "Compression") with TiffTag[Int]
  val PhotometricInterpretation = new ShortTag(262, "PhotometricInterpretation") with TiffTag[Int]
  val Orientation = new ShortTag(274, "Orientation") with TiffTag[Int]
  val SamplesPerPixel = new ShortTag(277, "SamplesPerPixel") with TiffTag[Int]
  val PlanarConfiguration = new ShortTag(284, "PlanarConfiguration") with TiffTag[Int]
  val YCbCrSubSampling = new ShortListTag(530, "YCbCrSubSampling") with TiffTag[List[Int]]
  val YCbCrPositioning = new ShortTag(531, "YCbCrPositioning") with TiffTag[Int]
  val ExifIfdPointer = new LongTag(0x8769, "ExifIfdPointer") with TiffTag[Long]
  val GpsInfoIfdPointer = new LongTag(0x8825, "GpsInfoIfdPointer") with TiffTag[Long]
  val XResolution = new RationalTag(282, "XResolution") with TiffTag[Rational]
  val YResolution = new RationalTag(283, "YResolution") with TiffTag[Rational]
  /**
   * 2 = inches, 3 = centimeters, others = reserved.
   * @see net.n12n.exif.TiffIfd#ResolutionInches net.n12n.exif.TiffIfd#ResolutionCms
   */
  val ResolutionUnit = new ShortTag(296, "ResolutionUnit") with TiffTag[Int]
  /** ``net.n12n.exif.TiffIfd.Resolution`` is in inches. */
  val ResolutionInches = 2
  /** ``net.n12n.exif.TiffIfd.Resolution`` is in centimeters. */
  val ResolutionCms = 3
  val StripOffsets = new NumericListTag(273, "StripOffsets") with TiffTag[List[Numeric]]
  val RowsPerStrip = new NumericTag(278, "RowsPerStrip") with TiffTag[Numeric]
  val StripByteCounts = new NumericListTag(279, "StripByteCounts")  with TiffTag[List[Numeric]]
  val JPEGInterchangeFormat = new LongTag(513, "JPEGInterchangeFormat") with TiffTag[Long]
  val JPEGInterchangeFormatLength = new LongTag(514, "JPEGInterchangeFormatLength") with TiffTag[Long]
  val TransferFunction = new ShortListTag(301, "TransferFunction") with TiffTag[List[Int]]
  val WhitePoint = new RationalListTag(318, "WhitePoint") with TiffTag[List[Rational]]
  val PrimaryChromaticities = new RationalListTag(319, "PrimaryChromaticities") with TiffTag[List[Rational]]
  val YCbCrCoefficients = new RationalListTag(529, "YCbCrCoefficients") with TiffTag[List[Rational]]
  val ReferenceBlackWhite = new RationalListTag(532, "ReferenceBlackWhite") with TiffTag[List[Rational]]
  val DateTime = new AsciiTag(306, "DateTime") with TiffTag[String]
  val ImageDescription = new AsciiTag(270, "ImageDescription") with TiffTag[String]
  val Make = new AsciiTag(271, "Make") with TiffTag[String]
  val Model = new AsciiTag(272, "Model") with TiffTag[String]
  val Software = new AsciiTag(305, "Software") with TiffTag[String]
  val Artist = new AsciiTag(315, "Artist") with TiffTag[String]
  val Copyright = new AsciiTag(33432, "Copyright") with TiffTag[String]
  
  val Tags = Set[TiffTag[_]](ImageWidth, ImageLength, BitsPerSample, Compression,
      PhotometricInterpretation,
    Orientation, SamplesPerPixel, PlanarConfiguration, YCbCrSubSampling, YCbCrPositioning,
    ExifIfdPointer, GpsInfoIfdPointer, XResolution, YResolution, ResolutionUnit, StripOffsets, 
    RowsPerStrip, StripByteCounts, JPEGInterchangeFormat, JPEGInterchangeFormatLength, 
    TransferFunction, WhitePoint, PrimaryChromaticities, YCbCrCoefficients, ReferenceBlackWhite, 
    DateTime, ImageDescription, Make, Model, Software, Artist, Copyright)
}
