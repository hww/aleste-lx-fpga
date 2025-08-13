
// src/main/scala/modules/i8255/top_level.scala
package  aleste.modules.pwm_dac

import spinal.core._

object TopLevel {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      targetDirectory = ".",
      device = Device.LATTICE,
      defaultConfigForClockDomains = ClockDomainConfig(
        resetKind = SYNC,
        resetActiveLevel = LOW
      )
    ).generateVerilog(new PwmDac)

    println("PwmDac Verilog generation completed successfully!")
  }
}