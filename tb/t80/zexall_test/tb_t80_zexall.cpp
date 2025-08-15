#include "Vt80.h"
#include "verilated.h"
#include "verilated_vcd_c.h"
#include <stdio.h>
#include <stdlib.h>

#define RAM_SIZE (64*1024)

vluint64_t main_time = 0;
VerilatedVcdC* tfp = NULL;

void load_rom(Vt80* top, const char* filename) {
    FILE* f = fopen(filename, "rb");
    if (!f) {
        printf("Error: could not open %s\n", filename);
        exit(1);
    }
    
    fseek(f, 0, SEEK_END);
    long size = ftell(f);
    fseek(f, 0, SEEK_SET);
    
    uint8_t* buf = (uint8_t*)malloc(size);
    fread(buf, 1, size, f);
    fclose(f);
    
    // Загрузка в "память" (в реальном тестбенче подключите RAM модуль)
    for (int i = 0; i < size; i++) {
        // Здесь нужно реализовать интерфейс с вашим RAM модулем
        // Например: top->ram->memory[i] = buf[i];
    }
    
    free(buf);
}

int main(int argc, char** argv) {
    Verilated::commandArgs(argc, argv);
    Vt80* top = new Vt80;
    
    // Инициализация трассировки
    Verilated::traceEverOn(true);
    tfp = new VerilatedVcdC;
    top->trace(tfp, 99);
    tfp->open("waveforms/t80_zexall.vcd");
    
    // Загрузка тестовой программы
    load_rom(top, "tb/zexall_test/data/zexall.com");
    
    // Инициализация
    top->reset_n = 0;
    top->clk = 0;
    
    // Сброс
    for (int i = 0; i < 10; i++) {
        top->clk = !top->clk;
        top->eval();
        tfp->dump(main_time++);
    }
    top->reset_n = 1;
    
    // Основной цикл симуляции
    int last_pc = -1;
    int timeout = 10000000; // Защита от бесконечного цикла
    
    while (!Verilated::gotFinish() && timeout--) {
        top->clk = !top->clk;
        top->eval();
        tfp->dump(main_time++);
        
        // Детектирование завершения теста (ZEXALL пишет в порт 0 по завершению)
        if (top->clk && top->iorq_n == 0 && top->wr_n == 0 && top->a == 0) {
            printf("Test completed with status: %02x\n", top->d_out);
            break;
        }
        
        // Проверка на зависание
        if (top->pc == last_pc && (main_time % 100) == 0) {
            printf("Warning: PC stuck at %04x\n", top->pc);
            break;
        }
        last_pc = top->pc;
    }
    
    // Завершение
    tfp->close();
    delete top;
    delete tfp;
    exit(0);
}