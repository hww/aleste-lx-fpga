package delta_sigma_dac

import spinal.core._
import spinal.core.sim._
import org.scalatest.flatspec.AnyFlatSpec

object DeltaSigmaDacTest {
  def main(args: Array[String]): Unit = {
    // Тест дельта-сигма ЦАП
    SimConfig.withWave.compile(new DeltaSigmaDac(16, 64)).doSim { dut =>
      dut.clockDomain.forkStimulus(10)
      
      // Тестовые значения (16 бит)
      val testValues = Seq(0x0000, 0x4000, 0x8000, 0xC000, 0xFFFF)
      
      for (value <- testValues) {
        dut.io.value #= value
        sleep(10000) // Ждём установления
        
        // Измеряем среднее значение
        var highCount = 0
        val totalCycles = 1024
        for (_ <- 0 until totalCycles) {
          if (dut.io.dacOut.toBoolean) highCount += 1
          dut.clockDomain.waitSampling()
        }
        
        val expected = value.toDouble / (1 << 16)
        val actual = highCount.toDouble / totalCycles
        val error = math.abs(expected - actual)
        
        assert(error < 0.01, 
          s"For value $value (${expected*100}%), got ${actual*100}% (error $error)")
      }
    }
  }
}