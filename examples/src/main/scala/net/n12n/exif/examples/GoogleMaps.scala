package net.n12n.exif.examples

import net.n12n.exif._
import java.io.FileInputStream

/**
 * Read GPS data from jpeg file and and print URL pointing to the place where the picture was 
 * taken in Google maps. Call with path to jpeg file as single argument.
 */
object GoogleMaps extends App {
  val uri = for {
    exif <- JpegMetaData(args(0)).exif
    lat <- exif.value(GpsIfd.GPSLatitude)
    lng <- exif.value(GpsIfd.GPSLongitude)
  } yield toMapsUri(lat, lng)
  println(uri)

  def toMapsUri(latitudeTag: List[Rational], longitudeTag: List[Rational]): String = {
    val List(latDeg, latMin, latSec) = latitudeTag
    val List(lonDeg, lonMin, lonSec) = longitudeTag
    val latitude = latDeg.toDouble() + latMin.toDouble() / 60 + latSec.toDouble() /3600
    val longitude = lonDeg.toDouble() + lonMin.toDouble() / 60 + lonSec.toDouble() / 3600
    s"https://maps.google.com/maps?ll=${latitude},${longitude}&ie=UTF8&z=16"
  }
}
