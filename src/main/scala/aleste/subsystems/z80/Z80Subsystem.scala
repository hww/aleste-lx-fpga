package aleste.subsystems.z80

import spinal.core._
import spinal.lib._

class Z80Subsystem extends Component {
  val io = new Bundle {
    val addr = out(UInt(16 bits))
    val data_in = in Bits(8 bits)
    val data_out = out(Bits(8 bits))
    val mem_rd = out(Bool())
    val mem_wr = out(Bool())
    val mem_mreq = out Bool()
    val mem_io = out Bool()
    val clk_en = in Bool()
    val reset_n = in Bool()
    val clk = in Bool()
  }

  val az80 = new BlackBox {
    // Ports
    val nM1 = out Bool()
    val nMREQ = out Bool()
    val nIORQ = out Bool()
    val nRD = out Bool()
    val nWR = out Bool()
    val nRFSH = out Bool()
    val nHALT = out Bool()
    val nBUSACK = out Bool()
    
    val nWAIT = in Bool()
    val nINT = in Bool()
    val nNMI = in Bool()
    val nRESET = in Bool()
    val nBUSRQ = in Bool()
    
    val CLK = in Bool()
    val A = out Bits(16 bits)
    val D = inout(Analog(Bits(8 bits)))

    // Configuration
    setDefinitionName("z80_top_direct_n")
    
    // Add all Verilog files
    AZ80Files.files.foreach(file => addRTLPath(file))
    
    noIoPrefix()
  }

  // Data bus handling
  val data_out_reg = Reg(Bits(8 bits)) init(0)
  when(az80.nRD)(data_out_reg := io.data_in)
  
  // Connections
  io.data_out := data_out_reg
  io.addr := az80.A.asUInt
  io.mem_mreq := !az80.nMREQ
  io.mem_io := !az80.nIORQ
  io.mem_rd := !az80.nRD
  io.mem_wr := !az80.nWR

  // Control signals
  az80.CLK := io.clk
  az80.nRESET := io.reset_n
  az80.nWAIT := True
  az80.nINT := True
  az80.nNMI := True
  az80.nBUSRQ := True
  
  // Data bus - using inout requires special handling
  az80.D := io.data_in
}

object AZ80Files {
  val files = Seq(
    "rtl/cores/a-z80/address_latch.v",
      "rtl/cores/a-z80/address_mux.v",
      "rtl/cores/a-z80/address_pins.v",
      "rtl/cores/a-z80/alu.v",
      "rtl/cores/a-z80/alu_bit_select.v",
      "rtl/cores/a-z80/alu_control.v",
      "rtl/cores/a-z80/alu_core.v",
      "rtl/cores/a-z80/alu_flags.v",
      "rtl/cores/a-z80/alu_mux_2.v",
      "rtl/cores/a-z80/alu_mux_2z.v",
      "rtl/cores/a-z80/alu_mux_3z.v",
      "rtl/cores/a-z80/alu_mux_4.v",
      "rtl/cores/a-z80/alu_mux_8.v",
      "rtl/cores/a-z80/alu_prep_daa.v",
      "rtl/cores/a-z80/alu_select.v",
      "rtl/cores/a-z80/alu_shifter_core.v",
      "rtl/cores/a-z80/alu_slice.v",
      "rtl/cores/a-z80/bus_control.v",
      "rtl/cores/a-z80/bus_switch.v",
      "rtl/cores/a-z80/clk_delay.v",
      "rtl/cores/a-z80/control_pins_n.v",
      "rtl/cores/a-z80/data_pins.v",
      "rtl/cores/a-z80/data_switch.v",
      "rtl/cores/a-z80/data_switch_mask.v",
      "rtl/cores/a-z80/decode_state.v",
      "rtl/cores/a-z80/execute.v",
      "rtl/cores/a-z80/inc_dec.v",
      "rtl/cores/a-z80/inc_dec_2bit.v",
      "rtl/cores/a-z80/interrupts.v",
      "rtl/cores/a-z80/ir.v",
      "rtl/cores/a-z80/memory_ifc.v",
      "rtl/cores/a-z80/pin_control.v",
      "rtl/cores/a-z80/pla_decode.v",
      "rtl/cores/a-z80/reg_control.v",
      "rtl/cores/a-z80/reg_file.v",
      "rtl/cores/a-z80/reg_latch.v",
      "rtl/cores/a-z80/resets.v",
      "rtl/cores/a-z80/sequencer.v",
      "rtl/cores/a-z80/z80_top_direct_n.v",
      // Добавляем include-файлы
      "rtl/cores/a-z80/core.vh",
      "rtl/cores/a-z80/coremodules.vh",
      "rtl/cores/a-z80/exec_matrix.vh",
      "rtl/cores/a-z80/exec_matrix_compiled.vh",
      "rtl/cores/a-z80/exec_module.vh",
      "rtl/cores/a-z80/exec_zero.vh",
      "rtl/cores/a-z80/globals.vh",
      "rtl/cores/a-z80/temp_wires.vh"

  )
}