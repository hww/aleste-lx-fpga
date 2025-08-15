// src/main/scala/modules/i8255/top_level.scala
package  aleste.modules.i8255

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
    ).generateVerilog(new I8255)

    println("i8255 Verilog generation completed successfully!")
  }
}