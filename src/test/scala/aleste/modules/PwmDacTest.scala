package aleste.modules.pwm_dac

import spinal.core._
import spinal.core.sim._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PwmDacTest extends AnyFlatSpec with Matchers {
  "PwmDac" should "correctly generate PWM signals" in {
    SimConfig.withWave.compile(new PwmDac(8)).doSim { dut =>
      // Ваша существующая логика теста 1
      dut.clockDomain.forkStimulus(10)
      val testValues = Seq(0, 64, 128, 192, 255)
      
      for (value <- testValues) {
        dut.io.value #= value
        sleep(1000)
        
        var highCount = 0
        for (_ <- 0 until 256) {
          if (dut.io.pwmOut.toBoolean) highCount += 1
          dut.clockDomain.waitSampling()
        }
        
        val expected = value
        val actual = highCount
        assert(math.abs(expected - actual) <= 1)
      }
    }
  }

  it should "handle clock divider correctly" in {
    SimConfig.withWave.compile(new PwmDac(8, 4)).doSim { dut =>
      // Ваша существующая логика теста 2
      dut.clockDomain.forkStimulus(10)
      dut.io.value #= 128
      
      var highCount = 0
      for (_ <- 0 until 256 * 16) {
        if (dut.io.pwmOut.toBoolean) highCount += 1
        dut.clockDomain.waitSampling()
      }
      
      assert(math.abs(128 * 16 - highCount) <= 16)
    }
  }
}