package net.n12n.exif
import java.io.IOException

/**
 * Simple command-line tool to view Exif segment content.
 */
object Exif extends App {
  try {
  	if (args.length != 1)
      usage()

    JpegMetaData(args(0)).exif.foreach(_.ifds.flatMap(_.attributes).foreach(
      attr => println(s"${attr.tag.name}: ${attr.value}")))
  } catch {
    case e: IOException => 
      System.err.println(s"Failed to open ${args(0)} for reading")
      System.exit(2)
  }
  
  private def usage() {
    System.err.println("Usage: Exif <path>")
    System.exit(1)
  }
}
