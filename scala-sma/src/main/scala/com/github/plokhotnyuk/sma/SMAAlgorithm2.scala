package com.github.plokhotnyuk.sma

import org.kot.test.Algorithm
import scalaz.concurrent.Actor._
import scalaz.concurrent.Actor

class SMAAlgorithm2(symbols: Array[String]) extends Algorithm {
  assert(symbols != null && symbols.length > 0, "Product list is not defined")

  private val buckets = symbols.map(symbol => (symbol, SMAAlgorithm2.createActor())).toMap

  def update(symbol: String, value: Int): Unit = buckets.get(symbol).map(_ ! Update(value))
}

object SMAAlgorithm2 {
  def createActor(): Actor[Command] = actor[Command] {
    val size = Algorithm.DEPTH
    val data = new Array[Int](size)
    var sum = 0
    var count = 0
    (cmd: Command) => cmd match {
      case Update(price) =>
        val pos = count % size
        sum += price - data(pos)
        data(pos) = price
        count += 1
      case GetAverage(callback) =>
        callback(sum / Math.min(count, size))
    }
  }
}

sealed abstract class Command
case class Update(price: Int) extends Command
case class GetAverage(callback: Int => Unit) extends Command