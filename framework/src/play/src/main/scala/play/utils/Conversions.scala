/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package play.utils

import scala.collection.{ immutable, mutable }

/**
 * provides conversion helpers
 */
object Conversions {

  def newMap[A, B](data: mutable.Map[A, B]): Map[A, B] = data.toMap
  def newMap[A, B](data: (A, B)*): Map[A, B] = Map(data: _*)

  def newSeq[A, B](data: collection.mutable.Map[A, B]): immutable.Seq[(A, B)] = data.toVector
}
