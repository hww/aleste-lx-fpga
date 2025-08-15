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
VHDL_WORKDIR := $(BUILD_DIR)/vhdl-work

# SBT Configuration
SBT := sbt -Djline.terminal=jline.UnsupportedTerminal -J-Xmx4G -J-XX:ParallelGCThreads=4 

# ECP5 Synthesis Settings
DEVICE := --um5g-85k --package CABGA381
CONSTRAINTS := constraints.lpf
TOP_MODULE := Aleste  # Must match your top module name in Verilog

# ======================== VHDL CONFIGURATION =======================

# Z80 Core Files
Z80_CORE_DIR := rtl/cores/t80
Z80_FILES := \
    $(Z80_CORE_DIR)/T80_Pack.vhd \
    $(Z80_CORE_DIR)/T80_MCode.vhd \
    $(Z80_CORE_DIR)/T80_ALU.vhd \
    $(Z80_CORE_DIR)/T80_Reg.vhd \
    $(Z80_CORE_DIR)/T80.vhd \
    $(Z80_CORE_DIR)/T80se.vhd


SRC_FILES += $(Z80_FILES)

# ====================== SPINALHDL CONFIGURATION ====================

# Module Hierarchy
MODULES := \
    aleste.modules.i8255 \
    aleste.modules.pwm_dac \
    aleste.modules.delta_sigma_dac \
    aleste

# Test Targets
TEST_SUITES := \
    aleste.modules.i8255_test \
    aleste.modules.pwm_dac_test \
    aleste.modules.delta_sigma_dac_test \
    aleste_test

# ======================== BUILD TARGETS ==========================
.PHONY: all clean generate test compile-vhdl init

all: generate test

# ----------------------- VHDL Compilation ------------------------
compile-vhdl: $(VHDL_WORKDIR)/.done

$(VHDL_WORKDIR)/.done: $(Z80_FILES)
	@echo "=== COMPILING VHDL (STRUCTURED) ==="
	@mkdir -p $(VHDL_WORKDIR) $(LOG_DIR)
	@cd $(VHDL_WORKDIR) && \
	for file in $(addprefix ../../,$(Z80_FILES)); do \
		echo "Compiling $$file..."; \
		ghdl -a --std=08 --work=work $$file || (echo "*** COMPILATION FAILED ***"; exit 1); \
	done | tee ../../$(LOG_DIR)/ghdl_compile.log
	@touch $(VHDL_WORKDIR)/.done

# ----------------------- RTL Generation --------------------------
generate: compile-vhdl $(addprefix gen-,$(MODULES))
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
	@$(SBT) "testOnly zexall.ZexallSpec" | tee $(REPORT_DIR)/zexall.log

# ====================== SYNTHESIS TARGETS =======================

synth-ecp5: generate
	@echo "=== STARTING SYNTHESIS ==="
	@mkdir -p $(SYNTH_DIR) $(LOG_DIR)
	yosys -p "read_verilog $(RTLS_DIR)/*.v; synth_ecp5 -top $(TOP_MODULE) -json $(SYNTH_DIR)/$(PROJECT_NAME).json" 2>&1 | tee $(LOG_DIR)/yosys.log
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
