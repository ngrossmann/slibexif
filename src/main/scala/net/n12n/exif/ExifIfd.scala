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
class ExifIfd(exif: ExifSegment, offset: Int) extends Ifd(exif, offset, ExifIfd.marker2tag)

case class ExifTag(marker: Int, name: String) extends Tag

object ExifIfd extends IfdObjectBase[ExifTag] {
  override protected val tag = (marker: Int, name: String) => ExifTag(marker, name)
  
  val PixelXDimension = ExifTag(40962, "PixelXDimension")
  val PixelYDimension = ExifTag(40963, "PixelYDimension")
  val ComponentsConfiguration = ExifTag(37121, "ComponentsConfiguration")
  val CompressedBitsPerPixel = ExifTag(37122, "CompressedBitsPerPixel")
  val ExifVersion = ExifTag(36864, "ExifVersion")
  val FlashpixVersion = ExifTag(40960, "FlashpixVersion")
  val MakerNote = ExifTag(37500, "MakerNote")
  val UserComment = ExifTag(37510, "UserComment")
  val RelatedSoundFile = ExifTag(40964, "RelatedSoundFile")
  val DateTimeOriginal = ExifTag(36867, "DateTimeOriginal")
  val DateTimeDigitized = ExifTag(36868, "DateTimeDigitized")
  val SubSecTime = ExifTag(37520, "SubSecTime")
  val SubSecTimeOriginal = ExifTag(37521, "SubSecTimeOriginal")
  val SubSecTimeDigitized = ExifTag(37522, "SubSecTimeDigitized")
  val ImageUniqueID = ExifTag(42016, "ImageUniqueID")

  val ExposureTime = ExifTag(33434, "ExposureTime")
  val FNumber = ExifTag(33437, "FNumber")
  val ExposureProgram = ExifTag(34850, "ExposureProgram")
  val SpectralSensitivity = ExifTag(34852, "SpectralSensitivity")
  val ISOSpeedRatings = ExifTag(34855, "ISOSpeedRatings")
  val OECF = ExifTag(34856, "OECF")
  val ShutterSpeedValue = ExifTag(37377, "ShutterSpeedValue")
  val ApertureValue = ExifTag(37378, "ApertureValue")
  val BrightnessValue = ExifTag(37379, "BrightnessValue")
  val ExposureBiasValue = ExifTag(37380, "ExposureBiasValue")
  val MaxApertureValue = ExifTag(37381, "MaxApertureValue")
  val SubjectDistance = ExifTag(37382, "SubjectDistance")
  val MeteringMode = ExifTag(37383, "MeteringMode")
  val LightSource = ExifTag(37384, "LightSource")
  val Flash = ExifTag(37385, "Flash")
  val FocalLength = ExifTag(37386, "FocalLength")
  val SubjectArea = ExifTag(37396, "SubjectArea")
  val FlashEnergy = ExifTag(41483, "FlashEnergy")
  val SpatialFrequencyResponse = ExifTag(41484, "SpatialFrequencyResponse")
  val FocalPlaneXResolution = ExifTag(41486, "FocalPlaneXResolution")
  val FocalPlaneYResolution = ExifTag(41487, "FocalPlaneYResolution")
  val FocalPlaneResolutionUnit = ExifTag(41488, "FocalPlaneResolutionUnit")
  val SubjectLocation = ExifTag(41492, "SubjectLocation")
  val ExposureIndex = ExifTag(41493, "ExposureIndex")
  val SensingMethod = ExifTag(41495, "SensingMethod")
  val FileSource = ExifTag(41728, "FileSource")
  val SceneType = ExifTag(41729, "SceneType")
  val CFAPattern = ExifTag(41730, "CFAPattern")
  val CustomRendered = ExifTag(41985, "CustomRendered")
  val ExposureMode = ExifTag(41986, "ExposureMode")
  val WhiteBalance = ExifTag(41987, "WhiteBalance")
  val DigitalZoomRatio = ExifTag(41988, "DigitalZoomRatio")
  val FocalLengthIn35mmFilm = ExifTag(41989, "FocalLengthIn35mmFilm")
  val SceneCaptureType = ExifTag(41990, "SceneCaptureType")
  val GainControl = ExifTag(41991, "GainControl")
  val Contrast = ExifTag(41992, "Contrast")
  val Saturation = ExifTag(41993, "Saturation")
  val Sharpness = ExifTag(41994, "Sharpness")
  val DeviceSettingDescription = ExifTag(41995, "DeviceSettingDescription")
  val SubjectDistanceRange = ExifTag(41996, "SubjectDistanceRange")
  
  override val Tags = Set(PixelXDimension, PixelYDimension,
    ComponentsConfiguration, CompressedBitsPerPixel, ExifVersion, FlashpixVersion, MakerNote,
    UserComment, RelatedSoundFile, DateTimeOriginal, DateTimeDigitized, SubSecTime,
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
