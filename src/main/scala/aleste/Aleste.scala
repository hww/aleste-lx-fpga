package aleste

import spinal.core._
import spinal.lib._

class Aleste extends Component {
  val io = new Bundle {
    val clk = in Bool()
    val in0 = in Bool()
    val out0 = out Bool()
  }
  io.out0 := io.in0
}