package com.github.plokhotnyuk.sma

import org.kot.test.Algorithm
import scalaz.concurrent.Actor._

class SMAAlgorithm2(symbols: Array[String]) extends Algorithm {
  assert(symbols != null && symbols.length > 0, "Product list is not defined")

  private val buckets = symbols.map(symbol => (symbol, actor[Int] {
    val size = Algorithm.DEPTH
    val data = new Array[Int](size)
    var sum = 0
    var pos = 0
    (price: Int) =>
      sum += price - data(pos)
      data(pos) = price
      pos = (pos + 1) % size
  })).toMap

  def update(symbol: String, value: Int): Unit = buckets.get(symbol).map(_ ! value)
}
