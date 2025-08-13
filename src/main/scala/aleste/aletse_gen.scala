// src/main/scala/modules/i8255/i8255_gen.scala
package aleste

import spinal.core._
import spinal.lib.io.TriState

object AlesteGen {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      targetDirectory = "rtl/verilog/",  // Путь относительно корня проекта
      device = Device.LATTICE,
      defaultConfigForClockDomains = ClockDomainConfig(
        resetKind = SYNC,
        resetActiveLevel = LOW
      )
    ).generateVerilog(new Aleste)
    
    println("[SUCCESS] Generated aleste.v")
  }
}


