// src/main/scala/modules/i8255/i8255_gen.scala
package i8255

import spinal.core._
import spinal.lib.io.TriState

object i8255Gen {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      targetDirectory = "rtl/verilog/",  // Путь относительно корня проекта
      device = Device.LATTICE,
      defaultConfigForClockDomains = ClockDomainConfig(
        resetKind = SYNC,
        resetActiveLevel = LOW
      )
    ).generateVerilog(new I8255)
    
    println("[SUCCESS] Generated i8255.v")
  }
}


