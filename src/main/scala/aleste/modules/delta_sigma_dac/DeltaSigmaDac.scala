package  aleste.modules.delta_sigma_dac

import spinal.core._
import spinal.lib._
/**
  * Дельта-сигма ЦАП с высоким разрешением.
  *
  * ==Принцип работы==
  * Использует модуляцию 1-го порядка для переноса шума квантования в ВЧ-область.
  *
  * ==Пример использования==
  * {{{
  * val dac = DeltaSigmaDac(16)
  * dac.io.value := audioSample
  * io.audioOut := dac.io.dacOut
  * }}}
  *
  * @param bitWidth            Разрядность входных данных (16-24 бита)
  * @param oversamplingRatio   Коэффициент передискретизации (>=64)
  * @see [[PwmDac]] Для простой ШИМ-реализации
  * @note Для аналоговой части требуется RC-фильтр 1-го порядка
  */
class DeltaSigmaDac(
    bitWidth: Int = 16,
    oversamplingRatio: Int = 64
) extends Component {
  require(bitWidth > 0, "Bit width must be positive")
  require(oversamplingRatio > 0, "Oversampling ratio must be positive")
  
  val io = new Bundle {
    val value = in UInt(bitWidth bits)
    val dacOut = out Bool()
  }

  // Аккумулятор для дельта-сигма модуляции
  val accumulator = Reg(UInt(bitWidth + 1 bits)) init(0)
  val feedback = Reg(UInt(bitWidth bits)) init(0)
  
  // Дельта-сигма модулятор первого порядка
  accumulator := accumulator + io.value.resized - feedback
  feedback := accumulator.msb ? U(1 << (bitWidth - 1)) | U(0)
  io.dacOut := accumulator.msb
}

