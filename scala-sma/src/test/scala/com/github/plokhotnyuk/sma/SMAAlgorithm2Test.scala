package com.github.plokhotnyuk.sma

import java.util.concurrent.{TimeUnit, CountDownLatch}
import org.junit.Assert._
import org.junit.Test
import org.kot.test.Algorithm
import org.scalacheck.Prop
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.{Test => ScalacheckTest}
import org.scalacheck.Test.Parameters

class SMAAlgorithm2Test {
  @Test def shouldCalcSumOfLastPrices(): Unit = check(forAll(nonEmptyListOf(choose(0, 999))) {
    (prices: List[Int]) =>
      val lastPrices = prices.takeRight(Algorithm.DEPTH)
      val expectedAverage = lastPrices.sum / lastPrices.size
      val completionFlag = new CountDownLatch(1)
      val actor = SMAAlgorithm2.createActor()
      prices.foreach(p => actor ! Update(p))
      actor ! GetAverage((a: Int) => if (a == expectedAverage) completionFlag.countDown())
      completionFlag.await(100, TimeUnit.MILLISECONDS)
  })

  private def check(p: Prop): Unit = assertTrue(ScalacheckTest.check(Parameters.defaultVerbose, p).passed)
}
