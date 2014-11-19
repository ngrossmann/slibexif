package net.n12n.exif
import java.io.IOException

/**
 * Simple command-line tool to view Exif segment content.
 */
object Exif extends App {
  try {
  	if (args.length != 1)
      usage()
    val image = JpegMetaData(args(0))
    for (exif <- image.exif) {
      println("Exif (length: %d):".format(exif.length))
      exif.ifds.foreach(printIfd)
      val xres: Rational = exif.ifd0.value(TiffIfd.XResolution)
    }
  } catch {
    case e: IOException => 
      System.err.println("Failed to open %s for reading".format(args(0)))
      System.exit(2)
  }
  
  private def usage() {
    System.err.println("Usage: Exif <path>")
    System.exit(1)
  }
  
  private def printIfd(ifd: Ifd) {
    println("IFD: %s".format(ifd.name))
    ifd.attributes.foreach(a => println("  %s: %s".format(a.tag.name, a.value)))
  }
}
