package aleste.subsystems.z80

import org.scalatest.flatspec.AnyFlatSpec
import spinal.core._
import spinal.core.sim._
import spinal.lib._

class ZexallSpec extends AnyFlatSpec {
  "Z80" should "pass ZEXALL" in {
    // Простая конфигурация без дополнительных параметров
    SimConfig
      .withWave
      .compile(new Z80TestBench)
      .doSim { dut =>
        // Настройка домена часов
        dut.clockDomain.forkStimulus(period = 10)
        
        // Сброс в начале
        dut.clockDomain.assertReset()
        sleep(100)
        dut.clockDomain.deassertReset()

        // Мониторинг выполнения
        var cycles = 0
        while(cycles < 1000000 && !dut.io.testDone.toBoolean) {
          dut.clockDomain.waitSampling()
          cycles += 1
          if(cycles % 100000 == 0) println(s"Cycle $cycles")
        }

        // Проверка результатов
        assert(dut.io.testDone.toBoolean, s"Timeout after $cycles cycles")
        assert(!dut.io.error.toBoolean, "Test failed with error flag")
        println(s"ZEXALL completed in $cycles cycles")
      }
  }
}