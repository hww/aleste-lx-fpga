// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See Vi8255.h for the primary calling header

#include "Vi8255__pch.h"
#include "Vi8255__Syms.h"
#include "Vi8255___024root.h"

void Vi8255___024root___ctor_var_reset(Vi8255___024root* vlSelf);

Vi8255___024root::Vi8255___024root(Vi8255__Syms* symsp, const char* v__name)
    : VerilatedModule{v__name}
    , vlSymsp{symsp}
 {
    // Reset structure values
    Vi8255___024root___ctor_var_reset(this);
}

void Vi8255___024root::__Vconfigure(bool first) {
    if (false && first) {}  // Prevent unused
}

Vi8255___024root::~Vi8255___024root() {
}
