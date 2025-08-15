#include "Valeste.h"
#include "verilated.h"

int main(int argc, char** argv) {
    Verilated::commandArgs(argc, argv);
    Valeste* top = new Valeste;
    
    // Test code here
    
    delete top;
    return 0;
}
