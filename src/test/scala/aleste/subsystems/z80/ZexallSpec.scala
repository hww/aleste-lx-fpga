package aleste.subsystems.z80

import org.scalatest.flatspec.AnyFlatSpec
import spinal.core._
import spinal.core.sim._

class ZexallSpec extends AnyFlatSpec {
  "Z80" should "pass ZEXALL" in {
    // Правильное название метода
    SimConfig
      .withGhdl  // <- Вот тут исправление
      .withConfig(SpinalConfig(
        defaultClockDomainFrequency = FixedFrequency(4 MHz)
      ))
      .compile(new Z80TestBench)
      .doSim { dut =>
        println("Simulation started")
        dut.clockDomain.forkStimulus(10)
        
        var cycles = 0
        while(cycles < 10000000 && !dut.io.testDone.toBoolean) {
          dut.clockDomain.waitSampling()
          cycles += 1
          if(cycles % 100000 == 0) println(s"Cycle $cycles")
        }
        
        assert(dut.io.testDone.toBoolean, s"Timeout at $cycles cycles!")
        assert(!dut.io.error.toBoolean, "Test failed!")
        println(s"ZEXALL completed in $cycles cycles")
      }
  }
}