package aleste

import spinal.core._
import spinal.core.sim._
import org.scalatest.flatspec.AnyFlatSpec

class SystemTest extends AnyFlatSpec {
  "Aleste" should "handle basic IO operations" in SimConfig.withWave.compile(new Aleste).doSim { dut => 
      simSuccess()
  }
}