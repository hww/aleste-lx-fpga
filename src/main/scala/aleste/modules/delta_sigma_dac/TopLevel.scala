// src/main/scala/modules/i8255/top_level.scala
package  aleste.modules.delta_sigma_dac

import spinal.core._

object TopLevel {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      targetDirectory = "build/rtl/",  // Путь относительно корня проекта 
      device = Device.LATTICE,
      defaultConfigForClockDomains = ClockDomainConfig(
        resetKind = SYNC,
        resetActiveLevel = LOW
      )
    ).generateVerilog(new DeltaSigmaDac)

    println("DeltaSigmaDac Verilog generation completed successfully!")
  }
}