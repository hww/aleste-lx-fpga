

// src/main/scala/modules/i8255/i8255_gen.scala
package  aleste.modules.delta_sigma_dac

import spinal.core._
import spinal.lib.io.TriState

object DeltaSigmaDacGen {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      targetDirectory = "build/rtl/",  // Путь относительно корня проекта
      device = Device.LATTICE,
      defaultConfigForClockDomains = ClockDomainConfig(
        resetKind = SYNC,
        resetActiveLevel = LOW
      )
    ).generateVerilog(new DeltaSigmaDac)
    
    println("[SUCCESS] Generated delta_sigma_dac.v")
  }
}


