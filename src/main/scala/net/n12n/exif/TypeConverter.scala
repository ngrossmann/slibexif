package net.n12n.exif

import java.nio.charset.Charset

import net.n12n.exif.ByteOrder._

private[exif] trait TypeConverter[T] {
  def toScala(attr: IfdAttribute, order: ByteOrder): List[T]
}

private[exif] abstract class SimpleTypeConverter[T](val id: Int, val size: Int, val name: String,
                                                      val stringLike: Boolean = false) extends TypeConverter[T] {
  def toScala(attr: IfdAttribute, order: ByteOrder): List[T] = {
    if (attr.typeId != id) {
      throw new IllegalArgumentException(
        s"Expected type ID ${id} but found ${attr.typeId} for IFD attribute ${attr.tag}")
    }
    if (stringLike) {
      List(toScala(attr.data, 0, order))
    } else {
      (for (i <- 0 until attr.count) yield toScala(attr.data, i * size, order)).toList
    }
  }

  protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder): T
}

private[exif] object TypeConverter {
  /** A rational value made up of two `net.n12n.exif.Type.Long` values. */
  implicit val rationalConverter = new SimpleTypeConverter[Rational](5, 8, "RATIONAL") {
    override protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toRational(offset, order)
  }

  implicit val byteConverter = new SimpleTypeConverter[ByteSeq](1, 1, "BYTE", true) {
    override protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.slice(offset, data.length - offset)
  }

  implicit val stringConverter = new SimpleTypeConverter[String](2, 1, "ASCII", true) {
    override protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.zstring(offset)
  }

  implicit val longConverter: SimpleTypeConverter[Long] = new SimpleTypeConverter[Long](4, 4, "LONG") {
    override protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toLong(offset, order)
  }

  implicit val shortConverter = new SimpleTypeConverter[Int](3, 2, "SHORT") {
    override protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toShort(offset, order)
  }

  implicit val undefinedConverter = new SimpleTypeConverter[Undefined](7, 1, "UNDEFINED", true) {
    override protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder) =
      new Undefined(data.slice(offset, data.length - offset).toArray())
  }

  implicit val signedRationalConverter = new SimpleTypeConverter[SignedRational](10,  8, "SRATIONAL") {
    override protected def toScala(data: ByteSeq, offset: Int, order: ByteOrder) = data.toSignedRational(offset, order)
  }

  implicit val numericConverter = new TypeConverter[Numeric]() {
    override def toScala(attr: IfdAttribute, order: ByteOrder): List[Numeric] = {
      if (attr.typeId == longConverter.id) {
        longConverter.toScala(attr, order).map(Numeric(_))
      } else if (attr.typeId == shortConverter.id) {
        shortConverter.toScala(attr, order).map(Numeric(_))
      } else
        throw new IllegalArgumentException("Type %d not supported".format(attr.typeId))
    }
  }

  implicit val multiByteStringConverter = new TypeConverter[MultiByteString] {
    override def toScala(attr: IfdAttribute, order: ByteOrder): List[MultiByteString] = {
      val charset = attr.data.zstring(0) match {
        case "ASCII" => Charset.forName("US-ASCII")
        case "JIS" => Charset.forName("")
        case "Unicode" => Charset.forName("UTF-8")
        case _ => Charset.defaultCharset()
      }
      List(MultiByteString(new String(attr.data.slice(8).toArray, charset), charset))
    }
  }
}
