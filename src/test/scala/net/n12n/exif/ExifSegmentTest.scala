package net.n12n.exif
import org.scalatest.FunSuite

class ExifSegmentTest extends FunSuite {
  val exif = new JpegMetaData(classOf[ExifSegmentTest].getResourceAsStream("image-gps.jpg")).
    exif match {
      case Some(e) => e
      case None => throw new Exception("Exif segment not found")
    }
  
  test("findAttr finds TiffAttribute") {
    assert(exif.findAttr(TiffIfd.XResolution) != None, "XResoultion not found in IFD0")
  }
  
  test("findAttr finds ExifAttribute") {
    assert(exif.findAttr(ExifIfd.ExifVersion) != None, "ExifVersion not found")
  }
  
  test("findAttr finds GpsAttribute") {
    assert(exif.findAttr(GpsIfd.GPSLongitude) != None, "GPSLongitude not found")
  }
  
  expectResult("A user comment", "Unicode UserComment found") {
    exif.exifIfd.get.value(ExifIfd.UserComment)
  }
}
