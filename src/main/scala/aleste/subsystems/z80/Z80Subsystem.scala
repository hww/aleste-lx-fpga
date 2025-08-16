package aleste.subsystems.z80

import spinal.core._
import spinal.lib._

class Z80Subsystem extends Component {
  val io = new Bundle {
    val addr = out UInt(16 bits)
    val data_in = in Bits(8 bits)
    val data_out = out Bits(8 bits)
    val mem_rd = out Bool()
    val mem_wr = out Bool()
    val mem_mreq = out Bool()
    val mem_io = out Bool()
    val clk_en = in Bool()
    val reset_n = in Bool()
    val clk = in Bool()  // Добавляем явный вход тактового сигнала
  }


  // Изолированный BlackBox без прямого подключения к clockDomain
  val t80box = new BlackBox {
    // Порты
    val RESET_n = in Bool()
    val CLK_n = in Bool()
    val CLKEN = in Bool()
    val WAIT_n = in Bool()
    val INT_n = in Bool()
    val NMI_n = in Bool()
    val BUSRQ_n = in Bool()
    val M1_n = out Bool()
    val MREQ_n = out Bool()
    val IORQ_n = out Bool()
    val RD_n = out Bool()
    val WR_n = out Bool()
    val RFSH_n = out Bool()
    val HALT_n = out Bool()
    val BUSAK_n = out Bool()
    val A = out Bits(16 bits)
    val DI = in Bits(8 bits)
    val DO = out Bits(8 bits)

    // Настройки
    setDefinitionName("T80se")
    addGeneric("Mode", 0)
    addGeneric("T2Write", 0)
    addGeneric("IOWait", 1)
    // Добавление VHDL файлов
    addRTLPath("rtl/cores/t80/T80_Pack.vhd")
    addRTLPath("rtl/cores/t80/T80_MCode.vhd")
    addRTLPath("rtl/cores/t80/T80_ALU.vhd")
    addRTLPath("rtl/cores/t80/T80_Reg.vhd")
    addRTLPath("rtl/cores/t80/T80.vhd")
    addRTLPath("rtl/cores/t80/T80se.vhd")
    
    // Явное подключение без использования clockDomain
    noIoPrefix()
  }

  // Подключение выходов
  io.data_out := t80box.DO
  io.addr := t80box.A.asUInt
  io.mem_mreq := !t80box.MREQ_n
  io.mem_io := !t80box.IORQ_n
  io.mem_rd := !t80box.RD_n
  io.mem_wr := !t80box.WR_n

  t80box.CLK_n := !io.clk
  t80box.RESET_n := io.reset_n
  t80box.CLKEN := io.clk_en
  t80box.WAIT_n := True
  t80box.INT_n := True
  t80box.NMI_n := True
  t80box.BUSRQ_n := True
  t80box.DI := io.data_in
}