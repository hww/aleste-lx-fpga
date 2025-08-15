// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Model implementation (design independent parts)

#include "Vi8255__pch.h"
#include "verilated_vcd_c.h"

//============================================================
// Constructors

Vi8255::Vi8255(VerilatedContext* _vcontextp__, const char* _vcname__)
    : VerilatedModel{*_vcontextp__}
    , vlSymsp{new Vi8255__Syms(contextp(), _vcname__, this)}
    , clk{vlSymsp->TOP.clk}
    , reset_n{vlSymsp->TOP.reset_n}
    , cs_n{vlSymsp->TOP.cs_n}
    , rd_n{vlSymsp->TOP.rd_n}
    , wr_n{vlSymsp->TOP.wr_n}
    , addr{vlSymsp->TOP.addr}
    , data{vlSymsp->TOP.data}
    , rootp{&(vlSymsp->TOP)}
{
    // Register model with the context
    contextp()->addModel(this);
}

Vi8255::Vi8255(const char* _vcname__)
    : Vi8255(Verilated::threadContextp(), _vcname__)
{
}

//============================================================
// Destructor

Vi8255::~Vi8255() {
    delete vlSymsp;
}

//============================================================
// Evaluation function

#ifdef VL_DEBUG
void Vi8255___024root___eval_debug_assertions(Vi8255___024root* vlSelf);
#endif  // VL_DEBUG
void Vi8255___024root___eval_static(Vi8255___024root* vlSelf);
void Vi8255___024root___eval_initial(Vi8255___024root* vlSelf);
void Vi8255___024root___eval_settle(Vi8255___024root* vlSelf);
void Vi8255___024root___eval(Vi8255___024root* vlSelf);

void Vi8255::eval_step() {
    VL_DEBUG_IF(VL_DBG_MSGF("+++++TOP Evaluate Vi8255::eval_step\n"); );
#ifdef VL_DEBUG
    // Debug assertions
    Vi8255___024root___eval_debug_assertions(&(vlSymsp->TOP));
#endif  // VL_DEBUG
    vlSymsp->__Vm_activity = true;
    vlSymsp->__Vm_deleter.deleteAll();
    if (VL_UNLIKELY(!vlSymsp->__Vm_didInit)) {
        vlSymsp->__Vm_didInit = true;
        VL_DEBUG_IF(VL_DBG_MSGF("+ Initial\n"););
        Vi8255___024root___eval_static(&(vlSymsp->TOP));
        Vi8255___024root___eval_initial(&(vlSymsp->TOP));
        Vi8255___024root___eval_settle(&(vlSymsp->TOP));
    }
    VL_DEBUG_IF(VL_DBG_MSGF("+ Eval\n"););
    Vi8255___024root___eval(&(vlSymsp->TOP));
    // Evaluate cleanup
    Verilated::endOfEval(vlSymsp->__Vm_evalMsgQp);
}

//============================================================
// Events and timing
bool Vi8255::eventsPending() { return false; }

uint64_t Vi8255::nextTimeSlot() {
    VL_FATAL_MT(__FILE__, __LINE__, "", "%Error: No delays in the design");
    return 0;
}

//============================================================
// Utilities

const char* Vi8255::name() const {
    return vlSymsp->name();
}

//============================================================
// Invoke final blocks

void Vi8255___024root___eval_final(Vi8255___024root* vlSelf);

VL_ATTR_COLD void Vi8255::final() {
    Vi8255___024root___eval_final(&(vlSymsp->TOP));
}

//============================================================
// Implementations of abstract methods from VerilatedModel

const char* Vi8255::hierName() const { return vlSymsp->name(); }
const char* Vi8255::modelName() const { return "Vi8255"; }
unsigned Vi8255::threads() const { return 1; }
void Vi8255::prepareClone() const { contextp()->prepareClone(); }
void Vi8255::atClone() const {
    contextp()->threadPoolpOnClone();
}
std::unique_ptr<VerilatedTraceConfig> Vi8255::traceConfig() const {
    return std::unique_ptr<VerilatedTraceConfig>{new VerilatedTraceConfig{false, false, false}};
};

//============================================================
// Trace configuration

void Vi8255___024root__trace_decl_types(VerilatedVcd* tracep);

void Vi8255___024root__trace_init_top(Vi8255___024root* vlSelf, VerilatedVcd* tracep);

VL_ATTR_COLD static void trace_init(void* voidSelf, VerilatedVcd* tracep, uint32_t code) {
    // Callback from tracep->open()
    Vi8255___024root* const __restrict vlSelf VL_ATTR_UNUSED = static_cast<Vi8255___024root*>(voidSelf);
    Vi8255__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    if (!vlSymsp->_vm_contextp__->calcUnusedSigs()) {
        VL_FATAL_MT(__FILE__, __LINE__, __FILE__,
            "Turning on wave traces requires Verilated::traceEverOn(true) call before time 0.");
    }
    vlSymsp->__Vm_baseCode = code;
    tracep->pushPrefix(std::string{vlSymsp->name()}, VerilatedTracePrefixType::SCOPE_MODULE);
    Vi8255___024root__trace_decl_types(tracep);
    Vi8255___024root__trace_init_top(vlSelf, tracep);
    tracep->popPrefix();
}

VL_ATTR_COLD void Vi8255___024root__trace_register(Vi8255___024root* vlSelf, VerilatedVcd* tracep);

VL_ATTR_COLD void Vi8255::trace(VerilatedVcdC* tfp, int levels, int options) {
    if (tfp->isOpen()) {
        vl_fatal(__FILE__, __LINE__, __FILE__,"'Vi8255::trace()' shall not be called after 'VerilatedVcdC::open()'.");
    }
    if (false && levels && options) {}  // Prevent unused
    tfp->spTrace()->addModel(this);
    tfp->spTrace()->addInitCb(&trace_init, &(vlSymsp->TOP));
    Vi8255___024root__trace_register(&(vlSymsp->TOP), tfp->spTrace());
}
