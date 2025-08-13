package pwm_dac

import spinal.core._
import spinal.core.sim._
import org.scalatest.flatspec.AnyFlatSpec

object PwmDacTest {
  def main(args: Array[String]): Unit = {
    // Тест 1: Проверка базовой функциональности
    SimConfig.withWave.compile(new PwmDac(8)).doSim { dut =>
      dut.clockDomain.forkStimulus(10)
      
      // Тестовые значения
      val testValues = Seq(0, 64, 128, 192, 255)
      
      for (value <- testValues) {
        dut.io.value #= value
        sleep(1000) // Ждём несколько периодов ШИМ
        
        // Проверяем скважность
        var highCount = 0
        for (_ <- 0 until 256) {
          if (dut.io.pwmOut.toBoolean) highCount += 1
          dut.clockDomain.waitSampling()
        }
        
        val expected = value
        val actual = highCount
        assert(math.abs(expected - actual) <= 1, 
          s"For value $expected, got duty $actual (expected $expected)")
      }
    }
    
    // Тест 2: Проверка с делителем частоты
    SimConfig.withWave.compile(new PwmDac(8, 4)).doSim { dut =>
      dut.clockDomain.forkStimulus(10)
      dut.io.value #= 128 // 50% заполнение
      
      // Считаем количество импульсов за период
      var highCount = 0
      for (_ <- 0 until 256 * 16) { // 16 - делитель
        if (dut.io.pwmOut.toBoolean) highCount += 1
        dut.clockDomain.waitSampling()
      }
      
      val expected = 128 * 16
      val actual = highCount
      assert(math.abs(expected - actual) <= 16, 
        s"Expected ~$expected pulses, got $actual")
    }
  }
}