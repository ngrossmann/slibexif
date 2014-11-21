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




