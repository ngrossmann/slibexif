package net.n12n.exif.examples

import net.n12n.exif._
import java.io.FileInputStream

/**
 * Read GPS data from jpeg file and and print URL pointing to the place where the picture was 
 * taken in Google maps. Call with path to jpeg file as single argument.
 */
object GoogleMaps extends App {
  require(args.length == 1)
  val exif = new JpegMetaData(new FileInputStream(args(0))).exif.get  
  val gpsIfd = exif.gpsIfd.get
  val List(latDeg, latMin, latSec) = gpsIfd.value(GpsIfd.GPSLatitude)
  val List(lonDeg, lonMin, lonSec) = gpsIfd.value(GpsIfd.GPSLongitude)
  val latitude = latDeg.toDouble() + latMin.toDouble() / 60 + latSec.toDouble() /3600
  val longitude = lonDeg.toDouble() + lonMin.toDouble() / 60 + lonSec.toDouble() / 3600
  println("https://maps.google.com/maps?ll=%f,%f&ie=UTF8&z=16".format(latitude, longitude))
}
