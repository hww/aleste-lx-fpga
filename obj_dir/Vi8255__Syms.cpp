// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Symbol table implementation internals

#include "Vi8255__pch.h"
#include "Vi8255.h"
#include "Vi8255___024root.h"

// FUNCTIONS
Vi8255__Syms::~Vi8255__Syms()
{
}

Vi8255__Syms::Vi8255__Syms(VerilatedContext* contextp, const char* namep, Vi8255* modelp)
    : VerilatedSyms{contextp}
    // Setup internal state of the Syms class
    , __Vm_modelp{modelp}
    // Setup module instances
    , TOP{this, namep}
{
    // Configure time unit / time precision
    _vm_contextp__->timeunit(-9);
    _vm_contextp__->timeprecision(-12);
    // Setup each module's pointers to their submodules
    // Setup each module's pointer back to symbol table (for public functions)
    TOP.__Vconfigure(true);
}
