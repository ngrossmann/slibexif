package net.n12n.exif

import net.n12n.exif.ByteOrder._

protected[exif] object TypeConverter {
  /** A rational value made up of two `net.n12n.exif.Type.Long` values. */
  implicit val rationalConverter = new TypeConverter[Rational](5, 8, "RATIONAL") {
    override def toScala(attr: IfdAttribute, order: ByteOrder) = {
      require(attr.typeId == id)
      require(attr.count == 1)
      attr.data.toRational(0, order)
    }
  }

}

protected[exif] abstract class TypeConverter[T](val id: Int, val size: Int, val name: String,
                                                val stringLike: Boolean = false) {
  def toScala(attr: IfdAttribute, order: ByteOrder): T
}

