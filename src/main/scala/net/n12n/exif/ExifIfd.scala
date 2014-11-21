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
 * IFD defined by EXIF standard.
 * 
 * @param exif The Exif segment containing this IFD.
 * @param offset Start of this IFD relative to the ``tiffOffset``.
 */
class ExifIfd(exif: ExifSegment, offset: Int) extends Ifd(exif, offset, "Exif IFD") {
  override type TagType = ExifTag[_]
  override val Tags = ExifIfd.Tags
}

trait ExifTag[T] extends TypedTag[T]

object ExifIfd {
  
  val PixelXDimension = new NumericTag(40962, "PixelXDimension") with ExifTag[Numeric]
  val PixelYDimension = new NumericTag(40963, "PixelYDimension") with ExifTag[Numeric]
  val ComponentsConfiguration = new UndefinedTag(37121, "ComponentsConfiguration") with ExifTag[Undefined]
  val CompressedBitsPerPixel = new RationalTag(37122, "CompressedBitsPerPixel") with ExifTag[Rational]
  val ExifVersion = new UndefinedTag(36864, "ExifVersion") with ExifTag[Undefined]
  val FlashpixVersion = new UndefinedTag(40960, "FlashpixVersion") with ExifTag[Undefined]
  val ColorSpace = new ShortTag(40961, "ColorSpace") with ExifTag[Int]
  val MakerNote = new UndefinedTag(37500, "MakerNote") with ExifTag[Undefined]
  val UserComment = new UserCommentTag(37510, "UserComment") with ExifTag[MultiByteString]
  val RelatedSoundFile = new AsciiTag(40964, "RelatedSoundFile") with ExifTag[String]
  val DateTimeOriginal = new AsciiTag(36867, "DateTimeOriginal") with ExifTag[String]
  val DateTimeDigitized = new AsciiTag(36868, "DateTimeDigitized") with ExifTag[String]
  val SubSecTime = new AsciiTag(37520, "SubSecTime") with ExifTag[String]
  val SubSecTimeOriginal = new AsciiTag(37521, "SubSecTimeOriginal") with ExifTag[String]
  val SubSecTimeDigitized = new AsciiTag(37522, "SubSecTimeDigitized") with ExifTag[String]
  val ImageUniqueID = new AsciiTag(42016, "ImageUniqueID") with ExifTag[String]

  val ExposureTime = new RationalTag(33434, "ExposureTime") with ExifTag[Rational]
  val FNumber = new RationalTag(33437, "FNumber") with ExifTag[Rational]
  val ExposureProgram = new ShortTag(34850, "ExposureProgram") with ExifTag[Int]
  val SpectralSensitivity = new AsciiTag(34852, "SpectralSensitivity") with ExifTag[String]
  val ISOSpeedRatings = new ShortListTag(34855, "ISOSpeedRatings") with ExifTag[List[Int]]
  val OECF = new UndefinedTag(34856, "OECF") with ExifTag[Undefined]
  val ShutterSpeedValue = new SignedRationalTag(37377, "ShutterSpeedValue") with ExifTag[SignedRational]
  val ApertureValue = new RationalTag(37378, "ApertureValue") with ExifTag[Rational]
  val BrightnessValue = new SignedRationalTag(37379, "BrightnessValue") with ExifTag[SignedRational]
  val ExposureBiasValue = new SignedRationalTag(37380, "ExposureBiasValue") with ExifTag[SignedRational]
  val MaxApertureValue = new RationalTag(37381, "MaxApertureValue") with ExifTag[Rational]
  val SubjectDistance = new RationalTag(37382, "SubjectDistance") with ExifTag[Rational]
  val MeteringMode = new ShortTag(37383, "MeteringMode") with ExifTag[Int]
  val LightSource = new ShortTag(37384, "LightSource") with ExifTag[Int]
  val Flash = new ShortTag(37385, "Flash") with ExifTag[Int]
  val FocalLength = new RationalTag(37386, "FocalLength") with ExifTag[Rational]
  val SubjectArea = new ShortListTag(37396, "SubjectArea") with ExifTag[List[Int]]
  val FlashEnergy = new RationalTag(41483, "FlashEnergy") with ExifTag[Rational]
  val SpatialFrequencyResponse = new UndefinedTag(41484, "SpatialFrequencyResponse") with ExifTag[Undefined]
  val FocalPlaneXResolution = new RationalTag(41486, "FocalPlaneXResolution") with ExifTag[Rational]
  val FocalPlaneYResolution = new RationalTag(41487, "FocalPlaneYResolution") with ExifTag[Rational]
  val FocalPlaneResolutionUnit = new ShortTag(41488, "FocalPlaneResolutionUnit") with ExifTag[Int]
  val SubjectLocation = new ShortListTag(41492, "SubjectLocation") with ExifTag[List[Int]]
  val ExposureIndex = new RationalTag(41493, "ExposureIndex") with ExifTag[Rational]
  /** Sensing mode, 1 not defined, 2 one-chip color area sensor, 3 two-chip color area sensor. */
  val SensingMethod = new ShortTag(41495, "SensingMethod") with ExifTag[Int]
  /** Source, 3 indicates the image was recorded by a digital still camera (DSC), other reserved. */
  val FileSource = new UndefinedTag(41728, "FileSource") with ExifTag[Undefined]
  /** Scene type, 1 indicates an directly photographed image, other reserved. */
  val SceneType = new UndefinedTag(41729, "SceneType") with ExifTag[Undefined]
  val CFAPattern = new UndefinedTag(41730, "CFAPattern") with ExifTag[Undefined]
  val CustomRendered = new ShortTag(41985, "CustomRendered") with ExifTag[Int]
  /** Exposure mode, 0 auto, 1 manual, 2 auto bracket, other reserved. */
  val ExposureMode = new ShortTag(41986, "ExposureMode") with ExifTag[Int]
  val WhiteBalance = new ShortTag(41987, "WhiteBalance") with ExifTag[Int]
  val DigitalZoomRatio = new RationalTag(41988, "DigitalZoomRatio") with ExifTag[Rational]
  val FocalLengthIn35mmFilm = new ShortTag(41989, "FocalLengthIn35mmFilm") with ExifTag[Int]
  val SceneCaptureType = new ShortTag(41990, "SceneCaptureType") with ExifTag[Int]
  val GainControl = new RationalTag(41991, "GainControl") with ExifTag[Rational]
  val Contrast = new ShortTag(41992, "Contrast") with ExifTag[Int]
  val Saturation = new ShortTag(41993, "Saturation") with ExifTag[Int]
  val Sharpness = new ShortTag(41994, "Sharpness") with ExifTag[Int]
  val DeviceSettingDescription = new UndefinedTag(41995, "DeviceSettingDescription") with ExifTag[Undefined]
  val SubjectDistanceRange = new ShortTag(41996, "SubjectDistanceRange") with ExifTag[Int]
  
  val Tags = Set[ExifTag[_]](PixelXDimension, PixelYDimension,
    ComponentsConfiguration, CompressedBitsPerPixel, ExifVersion, FlashpixVersion, ColorSpace,
    MakerNote, UserComment, RelatedSoundFile, DateTimeOriginal, DateTimeDigitized, SubSecTime,
    SubSecTimeOriginal, SubSecTimeDigitized, ImageUniqueID,
    ExposureTime, FNumber, ExposureProgram, SpectralSensitivity, ISOSpeedRatings, OECF,
    ShutterSpeedValue, ApertureValue, BrightnessValue, ExposureBiasValue, MaxApertureValue,
    SubjectDistance, MeteringMode, LightSource, Flash, FocalLength, SubjectArea, FlashEnergy,
    SpatialFrequencyResponse, FocalPlaneXResolution, FocalPlaneYResolution, 
    FocalPlaneResolutionUnit, SubjectLocation, ExposureIndex, SensingMethod, FileSource, SceneType,
    CFAPattern, CustomRendered, ExposureMode, WhiteBalance, DigitalZoomRatio, FocalLengthIn35mmFilm,
    SceneCaptureType, GainControl, Contrast, Saturation, Sharpness, DeviceSettingDescription,
    SubjectDistanceRange)
}
