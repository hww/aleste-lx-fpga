package  aleste.modules.i8255

import spinal.core._
import spinal.lib._

class I8255 extends Component {
  val io = new Bundle {
    // Control Interface
    val cs_n = in Bool()
    val rd_n = in Bool()
    val wr_n = in Bool()
    val addr = in UInt(2 bits)
    
    // Data Bus
    val data_in  = in Bits(8 bits)
    val data_out = out Bits(8 bits)
    val data_oe  = out Bool()
    
    // Ports
    val portA = new Bundle {
      val data_in  = in Bits(8 bits)
      val data_out = out Bits(8 bits)
      val dir_out  = out Bool()
    }
    val portB = new Bundle {
      val data_in  = in Bits(8 bits)
      val data_out = out Bits(8 bits)
      val dir_out  = out Bool()
    }
    val portC = new Bundle {
      val data_in  = in Bits(8 bits)
      val data_out = out Bits(8 bits)
      val dir_out  = out Bool()
    }
  }

  // Регистры
  val controlReg = Reg(Bits(8 bits)) init(0x9B)
  val portData = Vec(Reg(Bits(8 bits)) init(0), 3)
  val portDir = Vec(Reg(Bool()) init(False), 3)

  // Логика управления направлениями (исправленная)
  when(!io.cs_n && !io.wr_n && io.addr === 3) {
    controlReg := io.data_in
    
    // Правильное управление направлениями согласно документации 8255:
    // Бит 4: 0=PortA output, 1=PortA input
    // Бит 1: 0=PortB output, 1=PortB input
    // Бит 0: 0=PortC lower output, 1=PortC lower input
    portDir(0) := !io.data_in(4) // Port A direction
    portDir(1) := !io.data_in(1) // Port B direction
    portDir(2) := !io.data_in(0) // Port C direction
  }

  // Логика записи в порты
  when(!io.cs_n && !io.wr_n && io.addr =/= 3) {
    portData(io.addr) := io.data_in
  }

  // Логика чтения
  io.data_out := Mux(!io.cs_n && !io.rd_n,
    Mux(io.addr === 3, controlReg,
      Vec(io.portA.data_in, io.portB.data_in, io.portC.data_in)(io.addr)),
    B(0, 8 bits))
  
  io.data_oe := !io.cs_n && !io.rd_n

  // Управление портами
  io.portA.data_out := portData(0)
  io.portB.data_out := portData(1)
  io.portC.data_out := portData(2)
  
  io.portA.dir_out := portDir(0)
  io.portB.dir_out := portDir(1)
  io.portC.dir_out := portDir(2)
}