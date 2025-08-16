package aleste.subsystems.z80

import org.scalatest.flatspec.AnyFlatSpec
import spinal.core._
import spinal.core.sim._
import spinal.lib._

class ZexallSpec extends AnyFlatSpec {
  "A-Z80" should "pass ZEXALL" in {
    // Конфигурация симуляции с явным указанием Verilator
    val simConfig = SimConfig
      .withWave
      .withVerilator  // Явно указываем Verilator
      .allOptimisation  // Включаем оптимизации
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(
          resetKind = SYNC,
          resetActiveLevel = LOW  // Для A-Z80 сброс активен в 0
        )
      ))
      // Добавляем все файлы A-Z80 из корневой директории
      .addIncludeDir("rtl/cores/a-z80") 
      .addRtl("rtl/cores/a-z80/address_latch.v")
      .addRtl("rtl/cores/a-z80/address_mux.v")
      .addRtl("rtl/cores/a-z80/address_pins.v")
      .addRtl("rtl/cores/a-z80/alu.v")
      .addRtl("rtl/cores/a-z80/alu_bit_select.v")
      .addRtl("rtl/cores/a-z80/alu_control.v")
      .addRtl("rtl/cores/a-z80/alu_core.v")
      .addRtl("rtl/cores/a-z80/alu_flags.v")
      .addRtl("rtl/cores/a-z80/alu_mux_2.v")
      .addRtl("rtl/cores/a-z80/alu_mux_2z.v")
      .addRtl("rtl/cores/a-z80/alu_mux_3z.v")
      .addRtl("rtl/cores/a-z80/alu_mux_4.v")
      .addRtl("rtl/cores/a-z80/alu_mux_8.v")
      .addRtl("rtl/cores/a-z80/alu_prep_daa.v")
      .addRtl("rtl/cores/a-z80/alu_select.v")
      .addRtl("rtl/cores/a-z80/alu_shifter_core.v")
      .addRtl("rtl/cores/a-z80/alu_slice.v")
      .addRtl("rtl/cores/a-z80/bus_control.v")
      .addRtl("rtl/cores/a-z80/bus_switch.v")
      .addRtl("rtl/cores/a-z80/clk_delay.v")
      .addRtl("rtl/cores/a-z80/control_pins_n.v")
      .addRtl("rtl/cores/a-z80/data_pins.v")
      .addRtl("rtl/cores/a-z80/data_switch.v")
      .addRtl("rtl/cores/a-z80/data_switch_mask.v")
      .addRtl("rtl/cores/a-z80/decode_state.v")
      .addRtl("rtl/cores/a-z80/execute.v")
      .addRtl("rtl/cores/a-z80/inc_dec.v")
      .addRtl("rtl/cores/a-z80/inc_dec_2bit.v")
      .addRtl("rtl/cores/a-z80/interrupts.v")
      .addRtl("rtl/cores/a-z80/ir.v")
      .addRtl("rtl/cores/a-z80/memory_ifc.v")
      .addRtl("rtl/cores/a-z80/pin_control.v")
      .addRtl("rtl/cores/a-z80/pla_decode.v")
      .addRtl("rtl/cores/a-z80/reg_control.v")
      .addRtl("rtl/cores/a-z80/reg_file.v")
      .addRtl("rtl/cores/a-z80/reg_latch.v")
      .addRtl("rtl/cores/a-z80/resets.v")
      .addRtl("rtl/cores/a-z80/sequencer.v")
      .addRtl("rtl/cores/a-z80/z80_top_direct_n.v")


    // Компиляция с явным указанием имени верхнего модуля
    simConfig
      .compile {
        val tb = new Z80TestBench().setDefinitionName("z80testbench")
        // Дополнительно делаем сигналы доступными
        tb.z80.io.simPublic()
        tb
      }
      .doSim { dut =>
       // Настройка тактового сигнала
      dut.clockDomain.forkStimulus(period = 10)

      // Сброс (активный высокий уровень, как у вас в тестбенче)
      dut.clockDomain.assertReset()
      sleep(100)
      dut.clockDomain.deassertReset()
      
      // Мониторинг выполнения с проверкой PC
      var last_pc = 0
      var stable_pc_count = 0
      var cycles = 0
      val timeout = 1000000

      while(cycles < timeout && !dut.io.testDone.toBoolean) {
        dut.clockDomain.waitSampling()
        cycles += 1
        
        // Логирование каждые 10000 циклов
        if (cycles % 10000 == 0) {
          println(f"Cycle $cycles%8d: Addr=0x${dut.z80.io.addr.toInt}%04X")
        }

        // Детектор зависания (если адрес не меняется 1000 циклов)
        if (dut.z80.io.addr.toInt == last_pc) {
          stable_pc_count += 1
          if (stable_pc_count > 1000) {
            fail(s"Процессор завис на адресе 0x${last_pc}%04X")
          }
        } else {
          stable_pc_count = 0
          last_pc = dut.z80.io.addr.toInt
        }
      }
      
      // Проверка результатов
      assert(dut.io.testDone.toBoolean, s"Тест не завершился за $cycles циклов")
      assert(!dut.io.error.toBoolean, "Обнаружены ошибки в тесте")
      
      // Дополнительная проверка конечного адреса
      val finalAddr = dut.z80.io.addr.toInt
      println(s"Тест завершился на адресе 0x${finalAddr}%04X")
      
      // Проверяем, что завершились на корректном адресе
      if (finalAddr != 0x0000) {
        println(s"[WARNING] Завершение на неожиданном адресе 0x${finalAddr}%04X")
      }
      
      println(s"[SUCCESS] Тест пройден за $cycles циклов")
    }
  }
}