package aleste.subsystems.z80

import org.scalatest.flatspec.AnyFlatSpec
import spinal.core._
import spinal.core.sim._
import spinal.lib._

class ZexallSpec extends AnyFlatSpec {
  "Z80" should "pass ZEXALL" in {
    SimConfig
      .withGhdl
      .withConfig(SpinalConfig(
        defaultClockDomainFrequency = FixedFrequency(4 MHz)
      ))
      .addRtl("rtl/cores/t80/T80_Pack.vhd")
      .addRtl("rtl/cores/t80/T80_MCode.vhd")
      .addRtl("rtl/cores/t80/T80_ALU.vhd")
      .addRtl("rtl/cores/t80/T80_Reg.vhd")
      .addRtl("rtl/cores/t80/T80se.vhd")
      .compile(new Z80TestBench)
      .doSim { dut =>
        // Инициализация тактового сигнала
        dut.clockDomain.forkStimulus(period = 10)
        
        // Сброс в начале симуляции
        dut.clockDomain.assertReset()
        sleep(100)  // Задержка вместо deassertResetAfter
        dut.clockDomain.deassertReset()
        
        // Ожидание завершения теста
        var cycles = 0
        while(cycles < 1000000 && !dut.io.testDone.toBoolean) {
          dut.clockDomain.waitSampling()
          cycles += 1
          if(cycles % 100000 == 0) println(s"Cycle $cycles")
        }
        
        // Проверка результатов
        assert(dut.io.testDone.toBoolean, s"Timeout after $cycles cycles")
        assert(!dut.io.error.toBoolean, "Test failed with error flag")
      }
  }
}