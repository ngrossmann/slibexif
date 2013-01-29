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

/**
 * IFD defined by TIFF standard.
 *  
 * @param exif The Exif segment containing this IFD.
 * @param offset Start of this IFD relative to the [[de.n12n.exif.ExifSegment#tiffOffset]].
 */
class TiffIfd(exif: ExifSegment, offset: Int) extends Ifd(exif, offset, TiffIfd.marker2tag)

object TiffIfd {
  case class TiffTag(marker: Int, name: String) extends Tag
  
  val ImageWidth = TiffTag(256, "ImageWidth")
  val ImageLength = TiffTag(257, "ImageLength")
  val BitsPerSample = TiffTag(258, "BitsPerSample")
  val Compression = TiffTag(259, "Compression")
  val PhotometricInterpretation = TiffTag(262, "PhotometricInterpretation")
  val Orientation = TiffTag(274, "Orientation")
  val SamplesPerPixel = TiffTag(277, "SamplesPerPixel")
  val PlanarConfiguration = TiffTag(284, "PlanarConfiguration")
  val YCbCrSubSampling = TiffTag(530, "YCbCrSubSampling")
  val YCbCrPositioning = TiffTag(531, "YCbCrPositioning")
  val ExifIfdPointer = TiffTag(0x8769, "ExifIfdPointer")
  val GpsInfoIfdPointer = TiffTag(0x8825, "GpsInfoIfdPointer")
  val XResolution = TiffTag(282, "XResolution")
  val YResolution = TiffTag(283, "YResolution")
  val ResolutionUnit = TiffTag(296, "ResolutionUnit")
  val StripOffsets = TiffTag(273, "StripOffsets")
  val RowsPerStrip = TiffTag(278, "RowsPerStrip")
  val StripByteCounts = TiffTag(279, "StripByteCounts")
  val JPEGInterchangeFormat = TiffTag(513, "JPEGInterchangeFormat")
  val JPEGInterchangeFormatLength = TiffTag(514, "JPEGInterchangeFormatLength")
  val TransferFunction = TiffTag(301, "TransferFunction")
  val WhitePoint = TiffTag(318, "WhitePoint")
  val PrimaryChromaticities = TiffTag(319, "PrimaryChromaticities")
  val YCbCrCoefficients = TiffTag(529, "YCbCrCoefficients")
  val ReferenceBlackWhite = TiffTag(532, "ReferenceBlackWhite")
  val DateTime = TiffTag(306, "DateTime")
  val ImageDescription = TiffTag(270, "ImageDescription")
  val Make = TiffTag(271, "Make")
  val Model = TiffTag(272, "Model")
  val Software = TiffTag(305, "Software")
  val Artist = TiffTag(315, "Artist")
  val Copyright = TiffTag(33432, "Copyright")
  
  val Tags = Set(ImageWidth, ImageLength, BitsPerSample, Compression, PhotometricInterpretation,
    Orientation, SamplesPerPixel, PlanarConfiguration, YCbCrSubSampling, YCbCrPositioning,
    ExifIfdPointer, GpsInfoIfdPointer, XResolution, YResolution, ResolutionUnit, StripOffsets, 
    RowsPerStrip, StripByteCounts, JPEGInterchangeFormat, JPEGInterchangeFormatLength, 
    TransferFunction, WhitePoint, PrimaryChromaticities, YCbCrCoefficients, ReferenceBlackWhite, 
    DateTime, ImageDescription, Make, Model, Software, Artist, Copyright)
  
  def marker2tag(id: Int): TiffTag = Tags.find(_.marker == id) match {
    case Some(t) => t
    case None => TiffTag(id, "TiffTag")
  }
}