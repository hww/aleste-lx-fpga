// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "Vi8255__Syms.h"


void Vi8255___024root__trace_chg_0_sub_0(Vi8255___024root* vlSelf, VerilatedVcd::Buffer* bufp);

void Vi8255___024root__trace_chg_0(void* voidSelf, VerilatedVcd::Buffer* bufp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root__trace_chg_0\n"); );
    // Init
    Vi8255___024root* const __restrict vlSelf VL_ATTR_UNUSED = static_cast<Vi8255___024root*>(voidSelf);
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    if (VL_UNLIKELY(!vlSymsp->__Vm_activity)) return;
    // Body
    Vi8255___024root__trace_chg_0_sub_0((&vlSymsp->TOP), bufp);
}

void Vi8255___024root__trace_chg_0_sub_0(Vi8255___024root* vlSelf, VerilatedVcd::Buffer* bufp) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root__trace_chg_0_sub_0\n"); );
    // Init
    uint32_t* const oldp VL_ATTR_UNUSED = bufp->oldp(vlSymsp->__Vm_baseCode + 1);
    // Body
    if (VL_UNLIKELY(vlSelf->__Vm_traceActivity[1U])) {
        bufp->chgCData(oldp+0,(vlSelf->i8255__DOT__port_a),8);
        bufp->chgCData(oldp+1,(vlSelf->i8255__DOT__port_b),8);
        bufp->chgCData(oldp+2,(vlSelf->i8255__DOT__port_c),8);
        bufp->chgCData(oldp+3,(vlSelf->i8255__DOT__control_reg),8);
    }
    bufp->chgBit(oldp+4,(vlSelf->clk));
    bufp->chgBit(oldp+5,(vlSelf->reset_n));
    bufp->chgBit(oldp+6,(vlSelf->cs_n));
    bufp->chgBit(oldp+7,(vlSelf->rd_n));
    bufp->chgBit(oldp+8,(vlSelf->wr_n));
    bufp->chgCData(oldp+9,(vlSelf->addr),2);
    bufp->chgCData(oldp+10,(vlSelf->data),8);
}

void Vi8255___024root__trace_cleanup(void* voidSelf, VerilatedVcd* /*unused*/) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root__trace_cleanup\n"); );
    // Init
    Vi8255___024root* const __restrict vlSelf VL_ATTR_UNUSED = static_cast<Vi8255___024root*>(voidSelf);
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    // Body
    vlSymsp->__Vm_activity = false;
    vlSymsp->TOP.__Vm_traceActivity[0U] = 0U;
    vlSymsp->TOP.__Vm_traceActivity[1U] = 0U;
}
