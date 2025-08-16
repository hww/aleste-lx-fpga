package aleste.subsystems.z80

import org.scalatest.flatspec.AnyFlatSpec
import spinal.core._
import spinal.core.sim._
import spinal.lib._

class ZexallSpec extends AnyFlatSpec {
  "Z80" should "pass ZEXALL" in {
    // Конфигурация симуляции
    val simConfig = SimConfig
      .withWave  // Включить запись waveform
      .withGhdl  // Использовать GHDL
    
    // Добавление флагов (альтернативный способ для 1.10.1)
    simConfig.addSimulatorFlag("-fsynopsys")
    simConfig.addSimulatorFlag("-fexplicit")
    
    // Компиляция и запуск
    simConfig.compile {
      new Z80TestBench // Убедитесь, что Z80TestBench определён в этом пакете
    }.doSim { dut =>
      // Настройка тактового сигнала
      dut.clockDomain.forkStimulus(period = 10)
      
      // Сброс
      dut.clockDomain.assertReset()
      sleep(100)
      dut.clockDomain.deassertReset()
      
      // Выполнение теста
      var cycles = 0
      while(cycles < 100000 && !dut.io.testDone.toBoolean) {
        dut.clockDomain.waitSampling()
        cycles += 1
      }
      
      assert(dut.io.testDone.toBoolean, "Тест не завершился")
    }
  }
}