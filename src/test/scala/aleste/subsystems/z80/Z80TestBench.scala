package aleste.subsystems.z80

import spinal.core._
import spinal.lib._
import aleste.utils.BinaryTools

class Z80TestBench extends Component {
  val io = new Bundle {
    val testDone = out(Bool())
    val error = out(Bool())
  }

  // Объявляем все сигналы ДО их использования
  val z80 = new Z80Subsystem()
  val cpuClockEn = Reg(Bool()) init(False)
  val clkCounter = Reg(UInt(5 bits)) init(0)
  val ram = Mem(Bits(8 bits), 65536)
  val testDone = RegInit(False)

  // Инициализация памяти
  val initialContent = BinaryTools.loadToMem("tests/zexall/zexall.com")
  ram.init(initialContent ++ Seq.fill(65536 - initialContent.length)(B(0)))

  // Генератор тактового разрешения
  clkCounter := clkCounter + 1
  cpuClockEn := clkCounter === 24
  when(cpuClockEn) { clkCounter := 0 }
  z80.io.clk_en := cpuClockEn

  // Управление памятью
  z80.io.data_in := 0
  when(z80.io.mem_mreq) {
    z80.io.data_in := ram.readSync(z80.io.addr)
    when(z80.io.mem_wr) {
      ram.write(z80.io.addr, z80.io.data_out)
    }
  }

  // I/O обработчик (без преобразования в симуляции)
  when(z80.io.mem_io && z80.io.mem_wr && z80.io.addr === 0xFF) {
    // Просто выводим битовое представление
    println(s"Console output: ${z80.io.data_out}")
  }

  // Детектор завершения теста
  when(z80.io.addr === 0 && z80.io.mem_mreq) {
    testDone := True
  }
  
  io.testDone := testDone
  io.error := False
}