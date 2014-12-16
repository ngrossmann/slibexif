package net.n12n

/**
 * Library to read EXIF meta-data.
 *
 * Main entry point is [[net.n12n.exif.JpegMetaData]] and its companion object.
 *
 * Basic structure of JPEG meta-data is:
 *
 *  - [[net.n12n.exif.Segment]]s:
 *   - [[net.n12n.exif.ComSegment]]: Segment containing text comments.
 *   - [[net.n12n.exif.ExifSegment]]: Exif data.
 *    - [[net.n12n.exif.Ifd]]: Image file directory structure containing [[net.n12n.exif.IfdAttribute]]s.
 *
 * There are three types of IFDs, [[net.n12n.exif.TiffIfd]], [[net.n12n.exif.ExifIfd]] and
 * [[net.n12n.exif.GpsIfd]].
 */
package object exif {

}
