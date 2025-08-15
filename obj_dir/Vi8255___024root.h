// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design internal header
// See Vi8255.h for the primary calling header

#ifndef VERILATED_VI8255___024ROOT_H_
#define VERILATED_VI8255___024ROOT_H_  // guard

#include "verilated.h"


class Vi8255__Syms;

class alignas(VL_CACHE_LINE_BYTES) Vi8255___024root final : public VerilatedModule {
  public:

    // DESIGN SPECIFIC STATE
    VL_IN8(clk,0,0);
    VL_IN8(reset_n,0,0);
    VL_IN8(cs_n,0,0);
    VL_IN8(rd_n,0,0);
    VL_IN8(wr_n,0,0);
    VL_IN8(addr,1,0);
    VL_INOUT8(data,7,0);
    CData/*7:0*/ data__en0;
    CData/*7:0*/ i8255__DOT__port_a;
    CData/*7:0*/ i8255__DOT__port_b;
    CData/*7:0*/ i8255__DOT__port_c;
    CData/*7:0*/ i8255__DOT__control_reg;
    CData/*0:0*/ i8255__DOT____VdfgTmp_ha4755a45__0;
    CData/*0:0*/ __VstlFirstIteration;
    CData/*0:0*/ __VicoFirstIteration;
    CData/*0:0*/ __Vtrigprevexpr___TOP__clk__0;
    CData/*0:0*/ __Vtrigprevexpr___TOP__reset_n__0;
    CData/*0:0*/ __VactContinue;
    IData/*31:0*/ __VactIterCount;
    VlUnpacked<CData/*0:0*/, 2> __Vm_traceActivity;
    VlTriggerVec<1> __VstlTriggered;
    VlTriggerVec<1> __VicoTriggered;
    VlTriggerVec<1> __VactTriggered;
    VlTriggerVec<1> __VnbaTriggered;

    // INTERNAL VARIABLES
    Vi8255__Syms* const vlSymsp;

    // CONSTRUCTORS
    Vi8255___024root(Vi8255__Syms* symsp, const char* v__name);
    ~Vi8255___024root();
    VL_UNCOPYABLE(Vi8255___024root);

    // INTERNAL METHODS
    void __Vconfigure(bool first);
};


#endif  // guard
