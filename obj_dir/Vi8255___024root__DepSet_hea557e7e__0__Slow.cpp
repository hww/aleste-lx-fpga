// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See Vi8255.h for the primary calling header

#include "Vi8255__pch.h"
#include "Vi8255___024root.h"

VL_ATTR_COLD void Vi8255___024root___eval_static(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_static\n"); );
}

VL_ATTR_COLD void Vi8255___024root___eval_initial(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_initial\n"); );
    // Body
    vlSelf->__Vtrigprevexpr___TOP__clk__0 = vlSelf->clk;
    vlSelf->__Vtrigprevexpr___TOP__reset_n__0 = vlSelf->reset_n;
}

VL_ATTR_COLD void Vi8255___024root___eval_final(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_final\n"); );
}

#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__stl(Vi8255___024root* vlSelf);
#endif  // VL_DEBUG
VL_ATTR_COLD bool Vi8255___024root___eval_phase__stl(Vi8255___024root* vlSelf);

VL_ATTR_COLD void Vi8255___024root___eval_settle(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_settle\n"); );
    // Init
    IData/*31:0*/ __VstlIterCount;
    CData/*0:0*/ __VstlContinue;
    // Body
    __VstlIterCount = 0U;
    vlSelf->__VstlFirstIteration = 1U;
    __VstlContinue = 1U;
    while (__VstlContinue) {
        if (VL_UNLIKELY((0x64U < __VstlIterCount))) {
#ifdef VL_DEBUG
            Vi8255___024root___dump_triggers__stl(vlSelf);
#endif
            VL_FATAL_MT("rtl/i8255.sv", 3, "", "Settle region did not converge.");
        }
        __VstlIterCount = ((IData)(1U) + __VstlIterCount);
        __VstlContinue = 0U;
        if (Vi8255___024root___eval_phase__stl(vlSelf)) {
            __VstlContinue = 1U;
        }
        vlSelf->__VstlFirstIteration = 0U;
    }
}

#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__stl(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___dump_triggers__stl\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VstlTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if ((1ULL & vlSelf->__VstlTriggered.word(0U))) {
        VL_DBG_MSGF("         'stl' region trigger index 0 is active: Internal 'stl' trigger - first iteration\n");
    }
}
#endif  // VL_DEBUG

void Vi8255___024root___ico_sequent__TOP__0(Vi8255___024root* vlSelf);

VL_ATTR_COLD void Vi8255___024root___eval_stl(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_stl\n"); );
    // Body
    if ((1ULL & vlSelf->__VstlTriggered.word(0U))) {
        Vi8255___024root___ico_sequent__TOP__0(vlSelf);
    }
}

VL_ATTR_COLD void Vi8255___024root___eval_triggers__stl(Vi8255___024root* vlSelf);

VL_ATTR_COLD bool Vi8255___024root___eval_phase__stl(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_phase__stl\n"); );
    // Init
    CData/*0:0*/ __VstlExecute;
    // Body
    Vi8255___024root___eval_triggers__stl(vlSelf);
    __VstlExecute = vlSelf->__VstlTriggered.any();
    if (__VstlExecute) {
        Vi8255___024root___eval_stl(vlSelf);
    }
    return (__VstlExecute);
}

#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__ico(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___dump_triggers__ico\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VicoTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if ((1ULL & vlSelf->__VicoTriggered.word(0U))) {
        VL_DBG_MSGF("         'ico' region trigger index 0 is active: Internal 'ico' trigger - first iteration\n");
    }
}
#endif  // VL_DEBUG

#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__act(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___dump_triggers__act\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VactTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if ((1ULL & vlSelf->__VactTriggered.word(0U))) {
        VL_DBG_MSGF("         'act' region trigger index 0 is active: @(posedge clk or negedge reset_n)\n");
    }
}
#endif  // VL_DEBUG

#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__nba(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___dump_triggers__nba\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VnbaTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if ((1ULL & vlSelf->__VnbaTriggered.word(0U))) {
        VL_DBG_MSGF("         'nba' region trigger index 0 is active: @(posedge clk or negedge reset_n)\n");
    }
}
#endif  // VL_DEBUG

VL_ATTR_COLD void Vi8255___024root___ctor_var_reset(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___ctor_var_reset\n"); );
    // Body
    vlSelf->clk = VL_RAND_RESET_I(1);
    vlSelf->reset_n = VL_RAND_RESET_I(1);
    vlSelf->cs_n = VL_RAND_RESET_I(1);
    vlSelf->rd_n = VL_RAND_RESET_I(1);
    vlSelf->wr_n = VL_RAND_RESET_I(1);
    vlSelf->addr = VL_RAND_RESET_I(2);
    vlSelf->data = VL_RAND_RESET_I(8);
    vlSelf->data__en0 = 0;
    vlSelf->i8255__DOT__port_a = VL_RAND_RESET_I(8);
    vlSelf->i8255__DOT__port_b = VL_RAND_RESET_I(8);
    vlSelf->i8255__DOT__port_c = VL_RAND_RESET_I(8);
    vlSelf->i8255__DOT__control_reg = VL_RAND_RESET_I(8);
    vlSelf->i8255__DOT____VdfgTmp_ha4755a45__0 = 0;
    vlSelf->__Vtrigprevexpr___TOP__clk__0 = VL_RAND_RESET_I(1);
    vlSelf->__Vtrigprevexpr___TOP__reset_n__0 = VL_RAND_RESET_I(1);
    for (int __Vi0 = 0; __Vi0 < 2; ++__Vi0) {
        vlSelf->__Vm_traceActivity[__Vi0] = 0;
    }
}
