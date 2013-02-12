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
 * @param offset Start of this IFD relative to the [[net.n12n.exif.ExifSegment#tiffOffset]].
 */
class ExifIfd(exif: ExifSegment, offset: Int) extends Ifd(exif, offset, "Exif IFD") {
  override type T = ExifTag
  override val Tags = ExifIfd.Tags
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

class ExifTag(marker: Int, name: String) extends Tag(marker, name)

object ExifIfd {
  
  val PixelXDimension = new ExifTag(40962, "PixelXDimension") with NumericTag
  val PixelYDimension = new ExifTag(40963, "PixelYDimension") with NumericTag
  val ComponentsConfiguration = new ExifTag(37121, "ComponentsConfiguration") with UndefinedTag
  val CompressedBitsPerPixel = new ExifTag(37122, "CompressedBitsPerPixel") with RationalTag
  val ExifVersion = new ExifTag(36864, "ExifVersion") with UndefinedTag
  val FlashpixVersion = new ExifTag(40960, "FlashpixVersion") with UndefinedTag
  val ColorSpace = new ExifTag(40961, "ColorSpace") with ShortTag
  val MakerNote = new ExifTag(37500, "MakerNote") with UndefinedTag
  val UserComment = new ExifTag(37510, "UserComment") with UserCommentTag
  val RelatedSoundFile = new ExifTag(40964, "RelatedSoundFile") with AsciiTag
  val DateTimeOriginal = new ExifTag(36867, "DateTimeOriginal") with AsciiTag
  val DateTimeDigitized = new ExifTag(36868, "DateTimeDigitized") with AsciiTag
  val SubSecTime = new ExifTag(37520, "SubSecTime") with AsciiTag
  val SubSecTimeOriginal = new ExifTag(37521, "SubSecTimeOriginal") with AsciiTag
  val SubSecTimeDigitized = new ExifTag(37522, "SubSecTimeDigitized") with AsciiTag
  val ImageUniqueID = new ExifTag(42016, "ImageUniqueID") with AsciiTag

  val ExposureTime = new ExifTag(33434, "ExposureTime") with RationalTag
  val FNumber = new ExifTag(33437, "FNumber") with RationalTag
  val ExposureProgram = new ExifTag(34850, "ExposureProgram") with ShortTag
  val SpectralSensitivity = new ExifTag(34852, "SpectralSensitivity") with AsciiTag
  val ISOSpeedRatings = new ExifTag(34855, "ISOSpeedRatings") with ShortListTag
  val OECF = new ExifTag(34856, "OECF") with UndefinedTag
  val ShutterSpeedValue = new ExifTag(37377, "ShutterSpeedValue") with SignedRationalTag
  val ApertureValue = new ExifTag(37378, "ApertureValue") with RationalTag
  val BrightnessValue = new ExifTag(37379, "BrightnessValue") with SignedRationalTag
  val ExposureBiasValue = new ExifTag(37380, "ExposureBiasValue") with SignedRationalTag
  val MaxApertureValue = new ExifTag(37381, "MaxApertureValue") with RationalTag
  val SubjectDistance = new ExifTag(37382, "SubjectDistance") with RationalTag
  val MeteringMode = new ExifTag(37383, "MeteringMode") with ShortTag
  val LightSource = new ExifTag(37384, "LightSource") with ShortTag
  val Flash = new ExifTag(37385, "Flash") with ShortTag
  val FocalLength = new ExifTag(37386, "FocalLength") with RationalTag
  val SubjectArea = new ExifTag(37396, "SubjectArea") with ShortListTag
  val FlashEnergy = new ExifTag(41483, "FlashEnergy") with RationalTag
  val SpatialFrequencyResponse = new ExifTag(41484, "SpatialFrequencyResponse") with UndefinedTag
  val FocalPlaneXResolution = new ExifTag(41486, "FocalPlaneXResolution") with RationalTag
  val FocalPlaneYResolution = new ExifTag(41487, "FocalPlaneYResolution") with RationalTag
  val FocalPlaneResolutionUnit = new ExifTag(41488, "FocalPlaneResolutionUnit") with ShortTag
  val SubjectLocation = new ExifTag(41492, "SubjectLocation") with ShortListTag
  val ExposureIndex = new ExifTag(41493, "ExposureIndex") with RationalTag
  val SensingMethod = new ExifTag(41495, "SensingMethod") with ShortTag
  val FileSource = new ExifTag(41728, "FileSource") with UndefinedTag
  val SceneType = new ExifTag(41729, "SceneType") with UndefinedTag
  val CFAPattern = new ExifTag(41730, "CFAPattern") with UndefinedTag
  val CustomRendered = new ExifTag(41985, "CustomRendered") with ShortTag
  val ExposureMode = new ExifTag(41986, "ExposureMode") with ShortTag
  val WhiteBalance = new ExifTag(41987, "WhiteBalance") with ShortTag
  val DigitalZoomRatio = new ExifTag(41988, "DigitalZoomRatio") with RationalTag
  val FocalLengthIn35mmFilm = new ExifTag(41989, "FocalLengthIn35mmFilm") with ShortTag
  val SceneCaptureType = new ExifTag(41990, "SceneCaptureType") with ShortTag
  val GainControl = new ExifTag(41991, "GainControl") with RationalTag
  val Contrast = new ExifTag(41992, "Contrast") with ShortTag
  val Saturation = new ExifTag(41993, "Saturation") with ShortTag
  val Sharpness = new ExifTag(41994, "Sharpness") with ShortTag
  val DeviceSettingDescription = new ExifTag(41995, "DeviceSettingDescription") with UndefinedTag
  val SubjectDistanceRange = new ExifTag(41996, "SubjectDistanceRange") with ShortTag
  
  val Tags = Set[ExifTag with TypedTag[_]](PixelXDimension, PixelYDimension,
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
