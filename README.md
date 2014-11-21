slibexif
========

[![Build Status](https://travis-ci.org/ngrossmann/slibexif.svg?branch=master)](https://travis-ci.org/ngrossmann/libexif)

Scala library to read JPEG Exif data. The library has no dependencies on any other libraries including
`javax.imageio.*`.

Examples
--------

Read GPS attributes:

```scala
  for {
    exif <- JpegMetaData(args("image.jpg")).exif
    lat <- exif.value(GpsIfd.GPSLatitude)
    lng <- exif.value(GpsIfd.GPSLongitude)
  } yield println(s"${tos(lat)}, ${tos(lng)}")


  def tos(r: List[Rational]): String = {
    val List(deg, min, sec) = r
    s"""${deg.toDouble}°${min.toDouble}'${sec.toDouble}\""""
  }

```

Prints something like `48.0°9.0'47.12, 11.0°35.0'49.931`.

List all attributes of an image:

```scala
    JpegMetaData("image.jpg").exif.foreach(_.ifds.flatMap(_.attributes).foreach(
      attr => println(s"${attr.tag.name}: ${attr.value}")))

```

Prints

```
Make: Samsung
Model: Galaxy Nexus
Orientation: 1
XResolution: 72/1
YResolution: 72/1
ResolutionUnit: 2
DateTime: 2013:01:29 22:31:15
YCbCrPositioning: 1
ExifIfdPointer: 192
GpsInfoIfdPointer: 860
ImageWidth: 160
ImageLength: 120
Compression: 6
XResolution: 72/1
YResolution: 72/1
ResolutionUnit: 2
JPEGInterchangeFormat: 1284
JPEGInterchangeFormatLength: 3482
ExposureTime: 862/1000000
...
```
