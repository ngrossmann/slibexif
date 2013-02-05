package net.n12n.exif.examples

import net.n12n.exif._

object GoogleMaps extends App {
  val exif = new JpegMetaData(this.getClass().getResourceAsStream("image-gps.jpg")).exif match {
    case Some(exif) => exif
    case None => throw new Exception("Image does not contain Exif Segment")
  }
  
  val gpsIfd = exif.gpsIfd.get
  
  val List(latDeg, latMin, latSec) = gpsIfd.value(GpsIfd.GPSLatitude)
  val List(lonDeg, lonMin, lonSec) = gpsIfd.value(GpsIfd.GPSLongitude)
  val latitude = latDeg.toDouble() + latMin.toDouble() / 60 + latSec.toDouble() /3600
  val longitude = lonDeg.toDouble() + lonMin.toDouble() / 60 + lonSec.toDouble() / 3600
  println("https://maps.google.com/maps?ll=%f,%f&ie=UTF8&z=16".format(latitude, longitude))
}
