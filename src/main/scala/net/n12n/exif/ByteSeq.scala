/* 
 * libexif - Scala library to parse JPEG EXIF data.
 * Copyright (C) Niklas Grossmann
 * 
 * This file is part of libexif.
 *
 * libexif is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * libexif is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with libexif.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.n12n.exif

object ByteSeq {
  
  //val LittleEndian = ByteSeq("II")
  //val BigEndian = ByteSeq("MM")
  
  def apply(len: Int, in: Iterator[Int]): ByteSeq = {
    val a = new Array[Byte](len)
    for (i <- 0 until len)
      if (in.hasNext)
        a(i) = in.next.toByte
      else
        throw new IllegalArgumentException("Failed to read " + len + " bytes form data stream")
    new ByteSeq(a)
  }
  def apply(ints: Array[Int]) = new ByteSeq(ints.map(_.toByte))
  def apply(vals: Int*):ByteSeq = new ByteSeq(vals.map(_.toByte).toArray)
  def apply(s: String): ByteSeq = new ByteSeq(s.getBytes("ASCII"))
}

/**
 * A sequence of bytes which may be converted to different types.
 */
class ByteSeq(a: Array[Byte]) {
  import ByteOrder._
  private val array = a
  val length = array.length
  
  def bytes(i: Int): Byte = array(i)
  
  override def equals(other: Any): Boolean = other match {
    case bs: ByteSeq if (bs.length == length) => array.zip(bs.array).forall((t) => t._1 == t._2) 
    case _ => false;
  }

  override def hashCode() = array.product
  
  def slice(start:Int, end:Int) = new ByteSeq(array.slice(start, end))
  
  def toShort(offset: Int, byteOrder: ByteOrder) = toNumber(offset, byteOrder, Type.Short.size).toInt
  
  def toSignedShort(offset: Int, byteOrder: ByteOrder) = 
    toNumber(offset, byteOrder, Type.Short.size).toShort
  
  def toLong(offset: Int, byteOrder: ByteOrder) = toNumber(offset, byteOrder, Type.Long.size)
  
  def toSignedLong(offset: Int, byteOrder: ByteOrder) = 
    toNumber(offset, byteOrder, Type.Long.size).toInt
  
  def toRational(offset: Int, byteOrder: ByteOrder) = 
    new Rational(toLong(offset, byteOrder), toLong(offset + Type.Long.size, byteOrder))
  
  def toSignedRational(offset: Int, byteOrder: ByteOrder) = 
    new SignedRational(toSignedLong(offset, byteOrder), toSignedLong(offset + Type.Long.size, byteOrder))
  
  private def toNumber(offset: Int, byteOrder: ByteOrder, size: Int): Long = {
    val offsets = if (byteOrder == ByteOrder.LittleEndian) (offset until offset + size).reverse 
      else (offset until offset + size)  
    val values = offsets.map(bytes(_)).map(toLong(_)) // Values in big-endian order
    values.reduceLeft((n, m) => (n << 8) + m)
  }
  
  private def toLong(byte: Byte): Long = if (byte >= 0)  byte.toLong else 0x100L + byte
  
  override def toString() = "[" + array.map((b) => (if (b < 0) 256 + b else b.toInt).toHexString).mkString(" ") +
    "]"
  
  /**
   * Read a zero terminated string.
   * @param start Start of string.
   * @param encoding Character encoding.
   * @return Bytes converted to string.
   */
  def zstring(start: Int, encoding: String = "ASCII"): String = {
    val end = array.indexOf(0, start)
    new String(array, start, if (end == -1) array.length else end, encoding)
  }
}
