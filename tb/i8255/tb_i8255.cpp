#include <stdlib.h>
#include <stdio.h>
#include "Vi8255.h"
#include "verilated.h"
#include "verilated_vcd_c.h"

void print_state(Vi8255* dut) {
    printf("STATE: cs_n=%d rd_n=%d wr_n=%d addr=0x%X data=0x%X\n",
           dut->cs_n, dut->rd_n, dut->wr_n, dut->addr, dut->data);
}

int main(int argc, char** argv) {
    Verilated::commandArgs(argc, argv);
    Verilated::traceEverOn(true);
    
    Vi8255* dut = new Vi8255;
    VerilatedVcdC* vcd = new VerilatedVcdC;
    dut->trace(vcd, 99);
    vcd->open("waveforms/i8255.vcd");

    // Инициализация
    dut->clk = 0;
    dut->reset_n = 0;
    dut->cs_n = 1;
    dut->wr_n = 1;
    dut->rd_n = 1;
    dut->addr = 0;
    dut->data = 0;
    dut->eval();
    vcd->dump(0);

    // Тестовые данные
    const uint8_t test_data = 0x55;
    int error = 0;

    // Такт 1: Сброс
    dut->clk = 1; dut->eval(); vcd->dump(10);
    dut->clk = 0; dut->eval(); vcd->dump(20);
    print_state(dut);

    // Такт 2: Снятие сброса
    dut->reset_n = 1;
    dut->clk = 1; dut->eval(); vcd->dump(30);
    dut->clk = 0; dut->eval(); vcd->dump(40);
    print_state(dut);

    // Такт 3: Запись в порт A
    dut->cs_n = 0;
    dut->wr_n = 0;
    dut->addr = 0b00;
    dut->data = test_data;
    dut->clk = 1; dut->eval(); vcd->dump(50);
    print_state(dut);
    dut->clk = 0; dut->eval(); vcd->dump(60);

    // Такт 4: Чтение порта A
    dut->wr_n = 1;
    dut->rd_n = 0;
    dut->clk = 1; dut->eval(); vcd->dump(70);
    print_state(dut);
    
    // Проверка
    if (dut->data != test_data) {
        printf("ERROR: Expected 0x%X, got 0x%X\n", test_data, dut->data);
        error = 1;
    }
    
    dut->clk = 0; dut->eval(); vcd->dump(80);

    // Завершение
    vcd->close();
    delete dut;
    delete vcd;
    return error;
}