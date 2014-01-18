package com.github.plokhotnyuk.sma

import java.util.concurrent.{TimeUnit, CountDownLatch}
import org.junit.Assert._
import org.junit.Test
import org.kot.test.Algorithm
import org.scalacheck.Prop
import org.scalacheck.Prop._
import org.scalacheck.{Test => ScalacheckTest}
import org.scalacheck.Test.Parameters

class SMAAlgorithm2Test {
  @Test def shouldCalcSumOfLastPrices(): Unit = check(Prop.forAll {
    (prices: List[Int]) =>
      val completionFlag = new CountDownLatch(1)
      val actor = SMAAlgorithm2.createActor()
      prices.foreach(price => actor ! Update(price))
      var sum: Int = 0
      actor ! GetSum {
        (s: Int) =>
          sum = s
          completionFlag.countDown()
      }
      completionFlag.await(100, TimeUnit.MILLISECONDS)
      sum == prices.takeRight(Algorithm.DEPTH).sum
  })

  private def check(p: Prop): Unit = assertTrue(ScalacheckTest.check(Parameters.defaultVerbose, p).passed)
}
