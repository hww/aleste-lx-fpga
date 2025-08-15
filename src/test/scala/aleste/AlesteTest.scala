package aleste

import spinal.core._
import spinal.core.sim._
import org.scalatest.flatspec.AnyFlatSpec

class AlesteTest extends AnyFlatSpec {
  "i8255" should "handle basic IO operations" in SimConfig.withWave.compile(new Aleste).doSim { dut => 
      simSuccess()
  }
}