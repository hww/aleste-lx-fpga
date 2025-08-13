# Project Configuration
PROJECT_NAME := aleste
SBT := sbt -Djline.terminal=jline.UnsupportedTerminal -J-Xmx4G -J-XX:ParallelGCThreads=4 
SCALA_PKG := aleste
RTLS_DIR := rtl/verilog
SIM_DIR := simulation
REPORTS_DIR := reports
# Yosys/NextPNR для ECP5
SYNTH_DIR := synth
DEVICE := --um5g-85k --package CABGA381
CONSTRAINTS := constraints.lpf
TOP_MODULE := Aleste  # Must match your actual top module name in Verilog

# Module Hierarchy
MODULES := \
    aleste.modules.i8255 \
    aleste.modules.pwm_dac \
    aleste.modules.delta_sigma_dac \
    aleste

# Тестовые цели
TEST_SUITES := \
    aleste.modules.i8255_test \
    aleste.modules.pwm_dac_test \
    aleste.modules.delta_sigma_dac_test \
    aleste_test

.PHONY: all clean generate test

all: generate test

# Генерация RTL
generate: $(addprefix gen-,$(MODULES))

gen-%:
	@echo "Generating $*..."
	@mkdir -p $(RTLS_DIR)
	echo $(SBT) "runMain $*.TopLevel"
	@$(SBT) "runMain $*.TopLevel" > $(RTLS_DIR)/$*.log || (echo "Generation failed"; exit 1)

# Запуск тестов (ТАБУЛЯЦИЯ!)
test: test-unit test-integration test-system

test-unit:
	@echo "Running unit tests..."
	@$(SBT) "testOnly $(TEST_SUITES)" | tee $(REPORTS_DIR)/unit_tests.log

test-integration:
	@echo "Running integration tests..."
	@$(SBT) "testOnly $(SCALA_PKG).integration.*" | tee $(REPORTS_DIR)/integration_tests.log

test-system:
	@echo "Running system tests..."
	@$(SBT) "testOnly $(SCALA_PKG).system.*" | tee $(REPORTS_DIR)/system_tests.log

# Остальные правила (wave, doc, clean, init, report) остаются аналогичными

# Синтез ECP5 (ТАБУЛЯЦИЯ В КОМАНДАХ!)
#synth-ecp5: generate
synth-ecp5: 
	@echo "Running Yosys synthesis..."
	@mkdir -p $(SYNTH_DIR)
	@yosys -p "read_verilog $(RTLS_DIR)/*.v; synth_ecp5 -top $(TOP_MODULE) -json $(SYNTH_DIR)/$(PROJECT_NAME).json" 2>&1 | tee $(SYNTH_DIR)/yosys.log
# @yosys -p "read_verilog $(RTLS_DIR)/*.v; synth_ecp5 -json $(SYNTH_DIR)/$(PROJECT_NAME).json" 2>&1 | tee $(SYNTH_DIR)/yosys.log
	@echo "Running NextPNR..."
	@nextpnr-ecp5 $(DEVICE) --top $(TOP_MODULE) --json $(SYNTH_DIR)/$(PROJECT_NAME).json --lpf $(CONSTRAINTS) --textcfg $(SYNTH_DIR)/$(PROJECT_NAME)_out.config 2>&1 | tee $(SYNTH_DIR)/nextpnr.log
	@echo "Generating bitstream..."
	@ecppack --svf $(SYNTH_DIR)/$(PROJECT_NAME).svf $(SYNTH_DIR)/$(PROJECT_NAME)_out.config $(SYNTH_DIR)/$(PROJECT_NAME).bit 2>&1 | tee $(SYNTH_DIR)/ecppack.log
	@echo "Synthesis complete. Bitstream: $(SYNTH_DIR)/$(PROJECT_NAME).bit"