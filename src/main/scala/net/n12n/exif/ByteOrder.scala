/**
 *
 */
package net.n12n.exif

/**
 * @author niklas
 *
 */
object ByteOrder extends Enumeration {
  type ByteOrder = Value
  val LittleEndian = Value(1)
  val BigEndian = Value(2)
}
import ByteOrder._