# ====================== PROJECT CONFIGURATION ======================
# Project Metadata
PROJECT_NAME := aleste
SCALA_PKG := aleste

# Build Directory Structure
BUILD_DIR := build
LOG_DIR := $(BUILD_DIR)/logs
REPORT_DIR := $(BUILD_DIR)/reports
RTLS_DIR := $(BUILD_DIR)/rtl
SIM_DIR := $(BUILD_DIR)/sim
SYNTH_DIR := $(BUILD_DIR)/synth
VERILOG_WORKDIR := $(BUILD_DIR)/verilog-work

# SBT Configuration
SBT := sbt -Djline.terminal=jline.UnsupportedTerminal -J-Xmx4G -J-XX:ParallelGCThreads=4 

# ECP5 Synthesis Settings
DEVICE := --um5g-85k --package CABGA381
CONSTRAINTS := constraints.lpf
TOP_MODULE := Aleste  # Must match your top module name in Verilog

# ======================== VERILOG CONFIGURATION =======================

# A-Z80 Core Files
AZ80_CORE_DIR := rtl/cores/a-z80
AZ80_FILES := \
	$(AZ80_CORE_DIR)/address_latch.v \
	$(AZ80_CORE_DIR)/address_mux.v \
	$(AZ80_CORE_DIR)/address_pins.v \
	$(AZ80_CORE_DIR)/alu.v \
	$(AZ80_CORE_DIR)/alu_bit_select.v \
	$(AZ80_CORE_DIR)/alu_control.v \
	$(AZ80_CORE_DIR)/alu_core.v \
	$(AZ80_CORE_DIR)/alu_flags.v \
	$(AZ80_CORE_DIR)/alu_mux_2.v \
	$(AZ80_CORE_DIR)/alu_mux_2z.v \
	$(AZ80_CORE_DIR)/alu_mux_3z.v \
	$(AZ80_CORE_DIR)/alu_mux_4.v \
	$(AZ80_CORE_DIR)/alu_mux_8.v \
	$(AZ80_CORE_DIR)/alu_prep_daa.v \
	$(AZ80_CORE_DIR)/alu_select.v \
	$(AZ80_CORE_DIR)/alu_shifter_core.v \
	$(AZ80_CORE_DIR)/alu_slice.v \
	$(AZ80_CORE_DIR)/bus_control.v \
	$(AZ80_CORE_DIR)/bus_switch.v \
	$(AZ80_CORE_DIR)/clk_delay.v \
	$(AZ80_CORE_DIR)/control_pins_n.v \
	$(AZ80_CORE_DIR)/data_pins.v \
	$(AZ80_CORE_DIR)/data_switch.v \
	$(AZ80_CORE_DIR)/data_switch_mask.v \
	$(AZ80_CORE_DIR)/decode_state.v \
	$(AZ80_CORE_DIR)/execute.v \
	$(AZ80_CORE_DIR)/inc_dec.v \
	$(AZ80_CORE_DIR)/inc_dec_2bit.v \
	$(AZ80_CORE_DIR)/interrupts.v \
	$(AZ80_CORE_DIR)/ir.v \
	$(AZ80_CORE_DIR)/memory_ifc.v \
	$(AZ80_CORE_DIR)/pin_control.v \
	$(AZ80_CORE_DIR)/pla_decode.v \
	$(AZ80_CORE_DIR)/reg_control.v \
	$(AZ80_CORE_DIR)/reg_file.v \
	$(AZ80_CORE_DIR)/reg_latch.v \
	$(AZ80_CORE_DIR)/resets.v \
	$(AZ80_CORE_DIR)/sequencer.v \
	$(AZ80_CORE_DIR)/z80_top_direct_n.v


SRC_FILES += $(AZ80_FILES)

# ====================== SPINALHDL CONFIGURATION ====================

# Module Hierarchy
MODULES := \
    aleste.modules.i8255 \
    aleste.modules.pwm_dac \
    aleste.modules.delta_sigma_dac \
    aleste

# Test Targets
TEST_SUITES := \
    aleste.modules.pwm_dac.PwmDacTest \
    aleste.modules.delta_sigma_dac.DeltaSigmaDacTes \
    aleste.modules.i8255.I8255Test \
    aleste.AlesyteTest

# ======================== BUILD TARGETS ==========================
.PHONY: all clean generate test compile-verilog init

all: generate test

# ----------------------- Verilog Compilation ------------------------
compile-verilog: $(VERILOG_WORKDIR)/.done

$(VERILOG_WORKDIR)/.done: $(AZ80_FILES)
	@echo "=== PREPARING VERILOG FILES ==="
	@mkdir -p $(VERILOG_WORKDIR) $(LOG_DIR)
	@touch $(VERILOG_WORKDIR)/.done

# ----------------------- RTL Generation --------------------------
generate: compile-verilog $(addprefix gen-,$(MODULES))
	@echo "=== RTL GENERATION COMPLETE ==="

gen-%:
	@echo "=== GENERATING $* ==="
	@mkdir -p $(RTLS_DIR) $(LOG_DIR)
	@$(SBT) "runMain $*.TopLevel" > $(LOG_DIR)/$*_rtlgen.log 2>&1; \
	if [ $$? -ne 0 ]; then \
	    echo "*** GENERATION FAILED ***"; \
	    echo "Error details:"; \
	    grep -A 5 -B 5 -i "error" $(LOG_DIR)/$*_rtlgen.log || cat $(LOG_DIR)/$*_rtlgen.log; \
	    exit 1; \
	else \
	    echo "=== GENERATION SUCCEEDED ==="; \
	fi

# ======================= TEST TARGETS ===========================

test: test-unit test-integration test-system

test-unit:
	@echo "=== RUNNING UNIT TESTS ==="
	@mkdir -p $(REPORT_DIR)
	$(SBT) "testOnly $(TEST_SUITES)" 2>&1 | tee $(REPORT_DIR)/unit_tests.log

test-integration:
	@echo "=== RUNNING INTEGRATION TESTS ==="
	$(SBT) "testOnly $(SCALA_PKG).integration.*" 2>&1 | tee $(REPORT_DIR)/integration_tests.log

test-system:
	@echo "=== RUNNING SYSTEM TESTS ==="
	$(SBT) "testOnly $(SCALA_PKG).system.*" 2>&1 | tee $(REPORT_DIR)/system_tests.log

test-zexall:
	@echo "=== RUNNING ZEXALL TESTS ==="
	@mkdir -p $(REPORT_DIR)
	@$(SBT) "testOnly aleste.subsystems.z80.ZexallSpec" | tee $(REPORT_DIR)/zexall.log

# ====================== SYNTHESIS TARGETS =======================

synth-ecp5: generate
	@echo "=== STARTING SYNTHESIS ==="
	@mkdir -p $(SYNTH_DIR) $(LOG_DIR)
	yosys -p "read_verilog $(RTLS_DIR)/*.v $(AZ80_FILES); synth_ecp5 -top $(TOP_MODULE) -json $(SYNTH_DIR)/$(PROJECT_NAME).json" 2>&1 | tee $(LOG_DIR)/yosys.log
	nextpnr-ecp5 $(DEVICE) --top $(TOP_MODULE) --json $(SYNTH_DIR)/$(PROJECT_NAME).json --lpf $(CONSTRAINTS) --textcfg $(SYNTH_DIR)/$(PROJECT_NAME)_out.config 2>&1 | tee $(LOG_DIR)/nextpnr.log
	ecppack --svf $(SYNTH_DIR)/$(PROJECT_NAME).svf $(SYNTH_DIR)/$(PROJECT_NAME)_out.config $(SYNTH_DIR)/$(PROJECT_NAME).bit 2>&1 | tee $(LOG_DIR)/ecppack.log
	@echo "=== BITSTREAM GENERATED: $(SYNTH_DIR)/$(PROJECT_NAME).bit ==="

# ====================== UTILITY TARGETS ========================

# ----------------------- Waveform View --------------------------
wave:
	@echo "=== OPENING WAVEFORM ==="
	gtkwave $(SIM_DIR)/*.vcd &

# ----------------------- Documentation --------------------------
doc:
	@echo "[DOC] Generating documentation..."
	@$(SBT) "runMain $(PROJECT_NAME).DocGenerator"

# ----------------------- Clean Project --------------------------
clean:
	@echo "=== CLEANING PROJECT ==="
	rm -rf $(BUILD_DIR)
	$(SBT) clean

# ----------------------- Initialize Project ---------------------
init:
	@echo "=== INIT PROJECT FOLDERS ==="
	@mkdir -p $(RTLS_DIR) $(SIM_DIR) $(REPORTS_DIR) $(SYNTH_DIR)
	@$(SBT) update

# ----------------------- Generate Report ------------------------
report:
	@echo "=== GENERATE REPORTS ==="
	@$(SBT) "runMain $(PROJECT_NAME).ReportGenerator" 2>&1 | tee $(REPORT_DIR)/build_report.txt