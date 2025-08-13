package aleste.modules.i8255

import spinal.core._
import spinal.core.sim._
import org.scalatest.flatspec.AnyFlatSpec

class I8255Test extends AnyFlatSpec {
  "i8255" should "handle basic IO operations" in SimConfig.withWave.compile(new I8255).doSim { dut =>
    // Initialize
    dut.io.cs_n #= true
    dut.io.wr_n #= true
    dut.io.rd_n #= true
    dut.io.addr #= 0
    dut.io.data_in #= 0
    dut.clockDomain.forkStimulus(10)
    dut.clockDomain.waitSampling(5)

    // 1. Test Control Register Write (0x80 = все порты на вывод)
    println("Writing control register 0x80")
    dut.io.cs_n #= false
    dut.io.wr_n #= false
    dut.io.addr #= 3
    dut.io.data_in #= 0x80
    dut.clockDomain.waitSampling(3) // Даём время на установку
    
    // Debug output
    println(s"PortA.dir_out = ${dut.io.portA.dir_out.toBoolean}")
    println(s"PortB.dir_out = ${dut.io.portB.dir_out.toBoolean}")
    println(s"PortC.dir_out = ${dut.io.portC.dir_out.toBoolean}")
    
    // Verify port directions
    assert(dut.io.portA.dir_out.toBoolean, "PortA should be output after writing 0x80")
    assert(dut.io.portB.dir_out.toBoolean, "PortB should be output after writing 0x80")
    assert(dut.io.portC.dir_out.toBoolean, "PortC should be output after writing 0x80")

    // 2. Test Port Writing
    val testData = 0x55
    println(s"Writing 0x${testData.toHexString} to PortA")
    dut.io.addr #= 0 // Port A
    dut.io.data_in #= testData
    dut.clockDomain.waitSampling(2)

    // Verify port output
    assert(dut.io.portA.data_out.toInt == testData, 
      s"PortA output should be 0x${testData.toHexString}, got 0x${dut.io.portA.data_out.toInt.toHexString}")

    simSuccess()
  }
}