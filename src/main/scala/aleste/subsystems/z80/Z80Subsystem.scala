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
  }

  // BlackBox для T80 с правильными подключениями
  val t80 = new BlackBox {
    // Объявляем ВСЕ порты явно
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

    // Настройки BlackBox
    setDefinitionName("T80se")
    addGeneric("Mode", 0)
    addGeneric("T2Write", 0)
    addGeneric("IOWait", 1)

    // Подключение VHDL файлов через абсолютные пути
    addRTLPath(sys.env("PWD") + "/rtl/cores/t80/T80_Pack.vhd")
    addRTLPath(sys.env("PWD") + "/rtl/cores/t80/T80_MCode.vhd")
    addRTLPath(sys.env("PWD") + "/rtl/cores/t80/T80_ALU.vhd")
    addRTLPath(sys.env("PWD") + "/rtl/cores/t80/T80_Reg.vhd")
    addRTLPath(sys.env("PWD") + "/rtl/cores/t80/T80se.vhd")
  }

  // Правильное подключение тактового домена
  t80.CLK_n := !clockDomain.clock
  t80.RESET_n := io.reset_n
  
  // Подключение управляющих сигналов
  t80.CLKEN := io.clk_en
  t80.WAIT_n := True
  t80.INT_n := True
  t80.NMI_n := True
  t80.BUSRQ_n := True
  t80.DI := io.data_in

  // Подключение выходов
  io.data_out := t80.DO
  io.addr := t80.A.asUInt
  io.mem_mreq := !t80.MREQ_n
  io.mem_io := !t80.IORQ_n
  io.mem_rd := !t80.RD_n
  io.mem_wr := !t80.WR_n
}