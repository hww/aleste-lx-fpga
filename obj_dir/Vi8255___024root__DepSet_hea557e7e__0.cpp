// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See Vi8255.h for the primary calling header

#include "Vi8255__pch.h"
#include "Vi8255___024root.h"

VL_INLINE_OPT void Vi8255___024root___ico_sequent__TOP__0(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___ico_sequent__TOP__0\n"); );
    // Body
    vlSelf->i8255__DOT____VdfgTmp_ha4755a45__0 = (1U 
                                                  & ((~ (IData)(vlSelf->cs_n)) 
                                                     & (~ (IData)(vlSelf->rd_n))));
    vlSelf->data__en0 = ((IData)(vlSelf->i8255__DOT____VdfgTmp_ha4755a45__0)
                          ? ((0U == (IData)(vlSelf->addr))
                              ? 0xffU : ((1U == (IData)(vlSelf->addr))
                                          ? 0xffU : 
                                         ((2U == (IData)(vlSelf->addr))
                                           ? 0xffU : 
                                          ((3U == (IData)(vlSelf->addr))
                                            ? 0xffU
                                            : 0U))))
                          : 0U);
    vlSelf->data = ((IData)(vlSelf->data__en0) & (((IData)(vlSelf->i8255__DOT____VdfgTmp_ha4755a45__0)
                                                    ? 
                                                   ((0U 
                                                     == (IData)(vlSelf->addr))
                                                     ? (IData)(vlSelf->i8255__DOT__port_a)
                                                     : 
                                                    ((1U 
                                                      == (IData)(vlSelf->addr))
                                                      ? (IData)(vlSelf->i8255__DOT__port_b)
                                                      : 
                                                     ((2U 
                                                       == (IData)(vlSelf->addr))
                                                       ? (IData)(vlSelf->i8255__DOT__port_c)
                                                       : 
                                                      ((3U 
                                                        == (IData)(vlSelf->addr))
                                                        ? (IData)(vlSelf->i8255__DOT__control_reg)
                                                        : 0U))))
                                                    : 0U) 
                                                  & (IData)(vlSelf->data__en0)));
}

void Vi8255___024root___eval_ico(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_ico\n"); );
    // Body
    if ((1ULL & vlSelf->__VicoTriggered.word(0U))) {
        Vi8255___024root___ico_sequent__TOP__0(vlSelf);
    }
}

void Vi8255___024root___eval_triggers__ico(Vi8255___024root* vlSelf);

bool Vi8255___024root___eval_phase__ico(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_phase__ico\n"); );
    // Init
    CData/*0:0*/ __VicoExecute;
    // Body
    Vi8255___024root___eval_triggers__ico(vlSelf);
    __VicoExecute = vlSelf->__VicoTriggered.any();
    if (__VicoExecute) {
        Vi8255___024root___eval_ico(vlSelf);
    }
    return (__VicoExecute);
}

void Vi8255___024root___eval_act(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_act\n"); );
}

VL_INLINE_OPT void Vi8255___024root___nba_sequent__TOP__0(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___nba_sequent__TOP__0\n"); );
    // Body
    if (vlSelf->reset_n) {
        if ((1U & ((~ (IData)(vlSelf->cs_n)) & (~ (IData)(vlSelf->wr_n))))) {
            if ((1U & (~ ((IData)(vlSelf->addr) >> 1U)))) {
                if ((1U & (~ (IData)(vlSelf->addr)))) {
                    vlSelf->i8255__DOT__port_a = vlSelf->data;
                }
                if ((1U & (IData)(vlSelf->addr))) {
                    vlSelf->i8255__DOT__port_b = vlSelf->data;
                }
            }
            if ((2U & (IData)(vlSelf->addr))) {
                if ((1U & (~ (IData)(vlSelf->addr)))) {
                    vlSelf->i8255__DOT__port_c = vlSelf->data;
                }
                if ((1U & (IData)(vlSelf->addr))) {
                    vlSelf->i8255__DOT__control_reg 
                        = vlSelf->data;
                }
            }
        }
    } else {
        vlSelf->i8255__DOT__port_a = 0U;
        vlSelf->i8255__DOT__port_b = 0U;
        vlSelf->i8255__DOT__port_c = 0U;
        vlSelf->i8255__DOT__control_reg = 0x9bU;
    }
    vlSelf->data = ((IData)(vlSelf->data__en0) & (((IData)(vlSelf->i8255__DOT____VdfgTmp_ha4755a45__0)
                                                    ? 
                                                   ((0U 
                                                     == (IData)(vlSelf->addr))
                                                     ? (IData)(vlSelf->i8255__DOT__port_a)
                                                     : 
                                                    ((1U 
                                                      == (IData)(vlSelf->addr))
                                                      ? (IData)(vlSelf->i8255__DOT__port_b)
                                                      : 
                                                     ((2U 
                                                       == (IData)(vlSelf->addr))
                                                       ? (IData)(vlSelf->i8255__DOT__port_c)
                                                       : 
                                                      ((3U 
                                                        == (IData)(vlSelf->addr))
                                                        ? (IData)(vlSelf->i8255__DOT__control_reg)
                                                        : 0U))))
                                                    : 0U) 
                                                  & (IData)(vlSelf->data__en0)));
}

void Vi8255___024root___eval_nba(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_nba\n"); );
    // Body
    if ((1ULL & vlSelf->__VnbaTriggered.word(0U))) {
        Vi8255___024root___nba_sequent__TOP__0(vlSelf);
        vlSelf->__Vm_traceActivity[1U] = 1U;
    }
}

void Vi8255___024root___eval_triggers__act(Vi8255___024root* vlSelf);

bool Vi8255___024root___eval_phase__act(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_phase__act\n"); );
    // Init
    VlTriggerVec<1> __VpreTriggered;
    CData/*0:0*/ __VactExecute;
    // Body
    Vi8255___024root___eval_triggers__act(vlSelf);
    __VactExecute = vlSelf->__VactTriggered.any();
    if (__VactExecute) {
        __VpreTriggered.andNot(vlSelf->__VactTriggered, vlSelf->__VnbaTriggered);
        vlSelf->__VnbaTriggered.thisOr(vlSelf->__VactTriggered);
        Vi8255___024root___eval_act(vlSelf);
    }
    return (__VactExecute);
}

bool Vi8255___024root___eval_phase__nba(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_phase__nba\n"); );
    // Init
    CData/*0:0*/ __VnbaExecute;
    // Body
    __VnbaExecute = vlSelf->__VnbaTriggered.any();
    if (__VnbaExecute) {
        Vi8255___024root___eval_nba(vlSelf);
        vlSelf->__VnbaTriggered.clear();
    }
    return (__VnbaExecute);
}

#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__ico(Vi8255___024root* vlSelf);
#endif  // VL_DEBUG
#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__nba(Vi8255___024root* vlSelf);
#endif  // VL_DEBUG
#ifdef VL_DEBUG
VL_ATTR_COLD void Vi8255___024root___dump_triggers__act(Vi8255___024root* vlSelf);
#endif  // VL_DEBUG

void Vi8255___024root___eval(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval\n"); );
    // Init
    IData/*31:0*/ __VicoIterCount;
    CData/*0:0*/ __VicoContinue;
    IData/*31:0*/ __VnbaIterCount;
    CData/*0:0*/ __VnbaContinue;
    // Body
    __VicoIterCount = 0U;
    vlSelf->__VicoFirstIteration = 1U;
    __VicoContinue = 1U;
    while (__VicoContinue) {
        if (VL_UNLIKELY((0x64U < __VicoIterCount))) {
#ifdef VL_DEBUG
            Vi8255___024root___dump_triggers__ico(vlSelf);
#endif
            VL_FATAL_MT("rtl/i8255.sv", 3, "", "Input combinational region did not converge.");
        }
        __VicoIterCount = ((IData)(1U) + __VicoIterCount);
        __VicoContinue = 0U;
        if (Vi8255___024root___eval_phase__ico(vlSelf)) {
            __VicoContinue = 1U;
        }
        vlSelf->__VicoFirstIteration = 0U;
    }
    __VnbaIterCount = 0U;
    __VnbaContinue = 1U;
    while (__VnbaContinue) {
        if (VL_UNLIKELY((0x64U < __VnbaIterCount))) {
#ifdef VL_DEBUG
            Vi8255___024root___dump_triggers__nba(vlSelf);
#endif
            VL_FATAL_MT("rtl/i8255.sv", 3, "", "NBA region did not converge.");
        }
        __VnbaIterCount = ((IData)(1U) + __VnbaIterCount);
        __VnbaContinue = 0U;
        vlSelf->__VactIterCount = 0U;
        vlSelf->__VactContinue = 1U;
        while (vlSelf->__VactContinue) {
            if (VL_UNLIKELY((0x64U < vlSelf->__VactIterCount))) {
#ifdef VL_DEBUG
                Vi8255___024root___dump_triggers__act(vlSelf);
#endif
                VL_FATAL_MT("rtl/i8255.sv", 3, "", "Active region did not converge.");
            }
            vlSelf->__VactIterCount = ((IData)(1U) 
                                       + vlSelf->__VactIterCount);
            vlSelf->__VactContinue = 0U;
            if (Vi8255___024root___eval_phase__act(vlSelf)) {
                vlSelf->__VactContinue = 1U;
            }
        }
        if (Vi8255___024root___eval_phase__nba(vlSelf)) {
            __VnbaContinue = 1U;
        }
    }
}

#ifdef VL_DEBUG
void Vi8255___024root___eval_debug_assertions(Vi8255___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vi8255___024root___eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((vlSelf->clk & 0xfeU))) {
        Verilated::overWidthError("clk");}
    if (VL_UNLIKELY((vlSelf->reset_n & 0xfeU))) {
        Verilated::overWidthError("reset_n");}
    if (VL_UNLIKELY((vlSelf->cs_n & 0xfeU))) {
        Verilated::overWidthError("cs_n");}
    if (VL_UNLIKELY((vlSelf->rd_n & 0xfeU))) {
        Verilated::overWidthError("rd_n");}
    if (VL_UNLIKELY((vlSelf->wr_n & 0xfeU))) {
        Verilated::overWidthError("wr_n");}
    if (VL_UNLIKELY((vlSelf->addr & 0xfcU))) {
        Verilated::overWidthError("addr");}
}
#endif  // VL_DEBUG
