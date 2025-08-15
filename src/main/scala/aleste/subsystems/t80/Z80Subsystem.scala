package aleste.subsystems.z80

import spinal.core._
import spinal.lib._

class Z80Subsystem extends Component {
  val io = new Bundle {
    // Clock and Reset
    val clk = in(Bool)
    val reset_n = in(Bool)
    
    // Memory Interface
    val addr = out(UInt(16 bits))
    val data_in = in(Bits(8 bits))
    val data_out = out(Bits(8 bits))
    val mem_rd = out(Bool)
    val mem_wr = out(Bool)
    val mem_mreq = out(Bool)
    
    // IO Interface
    val io_rd = out(Bool)
    val io_wr = out(Bool)
    
    // Control Signals
    val wait_req = in(Bool)
    val int_n = in(Bool)
    val nmi_n = in(Bool)
    val busak_n = out(Bool)
  }

  // Instantiate Z80 wrapper
  val z80 = new BlackBox {
    // Define IO to match VHDL entity
    val io = new Bundle {
      val clk = in(Bool)
      val reset_n = in(Bool)
      val addr = out(UInt(16 bits))
      val data_in = in(Bits(8 bits))
      val data_out = out(Bits(8 bits))
      val mem_rd = out(Bool)
      val mem_wr = out(Bool)
      val mem_mreq = out(Bool)
      val io_rd = out(Bool)
      val io_wr = out(Bool)
      val wait_req = in(Bool)
      val int_n = in(Bool)
      val nmi_n = in(Bool)
      val busak_n = out(Bool)
    }
    
    // Map VHDL generics if needed
    addGeneric("G_SOME_PARAM", 42)
    
    // Reference the VHDL file
    addRTLPath("rtl/z80_wrapper.vhd")
    
    // Rename all signals to match VHDL
    noIoPrefix()
  }

  // Direct connection mapping
  z80.io.clk := io.clk
  z80.io.reset_n := io.reset_n
  
  // Memory interface
  io.addr := z80.io.addr
  z80.io.data_in := io.data_in
  io.data_out := z80.io.data_out
  io.mem_rd := z80.io.mem_rd
  io.mem_wr := z80.io.mem_wr
  io.mem_mreq := z80.io.mem_mreq
  
  // IO interface
  io.io_rd := z80.io.io_rd
  io.io_wr := z80.io.io_wr
  
  // Control signals
  z80.io.wait_req := io.wait_req
  z80.io.int_n := io.int_n
  z80.io.nmi_n := io.nmi_n
  io.busak_n := z80.io.busak_n

  // Optional: Add clock domain crossing if needed
  // val z80ClockDomain = ClockDomain(
  //   clock = io.clk,
  //   reset = io.reset_n,
  //   config = ClockDomainConfig(
  //     resetActiveLevel = LOW
  //   )
  // )
}