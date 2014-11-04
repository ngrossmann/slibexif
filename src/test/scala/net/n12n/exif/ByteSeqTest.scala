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

import org.scalatest.FunSuite

/**
 * @author niklas
 *
 */
class ByteSeqTest extends FunSuite {
  private val array = Array[Byte](0, 0, 0, 1, 1, 0, 0, 0, 0xf, 0xf, 0xf)
  private val bseq = new ByteSeq(array)
  
  test("ByteSequence form Int Array") {
    val seq = ByteSeq(Array(0, 2, 127, 128, 255))
    assert(seq.bytes(0) == 0)
    assert(seq.bytes(1) == 2)
    assert(seq.bytes(2) == 127)
    assert(seq.bytes(3) == -128)
    assert(seq.bytes(4) == -1)
  }

  test("ByteSequence from Iterator") {
    val i = Seq(1, 2, 3, 4).iterator
    val bs = ByteSeq(4, i)
    assert(!i.hasNext)
  }
  
  expectResult(0x01000000, "toInt LittleEndian") {
    bseq.toSignedLong(0, ByteOrder.LittleEndian) 
  }
  
  expectResult(0x01, "toInt BigEndian") {
    bseq.toSignedLong(0, ByteOrder.BigEndian) 
  }
  
  expectResult(42, "Signed short") {
    val order = new ByteSeq(Array(0x4d, 0x4d))
    val marker = new ByteSeq(Array(0x00, 0x2a))
    marker.toSignedShort(0, ByteOrder.BigEndian)
  }
}
