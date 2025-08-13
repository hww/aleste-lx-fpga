// src/main/scala/i8255/Main.scala
package aleste

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
    ).generateVerilog(new Aleste)
    
    println("Aleste Verilog generation completed successfully!")
  }
}