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
  }

  // Создаем отдельный компонент-обертку
  val t80Wrapper = new Component {
    val io = new Bundle {
      val clk_en = in Bool()
      val data_in = in Bits(8 bits)
      val data_out = out Bits(8 bits)
      val addr = out UInt(16 bits)
      val mem_ctrl = out Bits(4 bits) // MREQ, IORQ, RD, WR
    }

    val t80 = new BlackBox {
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

      // Конфигурация
      setDefinitionName("T80se")
      addGeneric("Mode", 0)
      addGeneric("T2Write", 0)
      addGeneric("IOWait", 1)
      addRTLPath("rtl/cores/t80/T80_Pack.vhd")
      addRTLPath("rtl/cores/t80/T80_MCode.vhd")
      addRTLPath("rtl/cores/t80/T80_ALU.vhd")
      addRTLPath("rtl/cores/t80/T80_Reg.vhd")
      addRTLPath("rtl/cores/t80/T80se.vhd")

      // Подключения
      mapCurrentClockDomain(clock = CLK_n, reset = RESET_n)
      RESET_n := !clockDomain.reset
      CLK_n := !clockDomain.clock
      CLKEN := io.clk_en
      WAIT_n := True
      INT_n := True
      NMI_n := True
      BUSRQ_n := True
      DI := io.data_in
    }

    // Выходы
    io.data_out := t80.DO
    io.addr := t80.A.asUInt
    io.mem_ctrl := B(t80.MREQ_n, t80.IORQ_n, t80.RD_n, t80.WR_n)
  }

  // Внешние подключения
  t80Wrapper.io.clk_en := io.clk_en
  t80Wrapper.io.data_in := io.data_in
  io.data_out := t80Wrapper.io.data_out
  io.addr := t80Wrapper.io.addr
  
  val mem_ctrl = t80Wrapper.io.mem_ctrl
  io.mem_mreq := !mem_ctrl(3)
  io.mem_io := !mem_ctrl(2)
  io.mem_rd := !mem_ctrl(1)
  io.mem_wr := !mem_ctrl(0)
}