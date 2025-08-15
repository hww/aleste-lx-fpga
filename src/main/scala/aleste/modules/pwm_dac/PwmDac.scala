package  aleste.modules.pwm_dac

import spinal.core._
import spinal.lib._
import spinal.core.sim._

// 

/**
  * Класс ЦАП с ШИМ-модуляцией.
  *
  * @param bitWidth           Разрядность ЦАП (количество бит)
  * @param clockDividerWidth  Ширина счётчика делителя частоты (0 для отключения)
  * 
  * ==Пример использования==
  * val pwmDac = PwmDac(bitWidth = 10) // 10-битный ЦАП
  * io.dacOut := pwmDac.io.pwmOut
  * pwmDac.io.value := audioSample.resized
  */
case class PwmDac(
    bitWidth: Int = 8,
    clockDividerWidth: Int = 0
) extends Component {
  require(bitWidth > 0, "Bit width must be positive")
  
  val io = new Bundle {
    val value = in UInt(bitWidth bits)
    val pwmOut = out Bool()
  }

  // Счётчик для генерации ШИМ
  val counter = Reg(UInt(bitWidth bits)) init(0)
  counter := counter + 1
  
  // Сравнение с входным значением
  io.pwmOut := counter < io.value
  
  // Опциональный делитель частоты
  if (clockDividerWidth > 0) {
    val clockDivider = Reg(UInt(clockDividerWidth bits)) init(0)
    val clockEnable = False
    
    clockDivider := clockDivider + 1
    when(clockDivider === 0) {
      clockEnable := True
    }
    
    // Обновляем значение только по разрешению
    val valueSampled = RegNextWhen(io.value, clockEnable)
    when(clockEnable) {
      counter := counter + 1
      io.pwmOut := counter < valueSampled
    }
  }
}