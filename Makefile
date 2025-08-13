# Project Configuration
PROJECT_NAME := aleste
SBT := sbt -Djline.terminal=jline.UnsupportedTerminal
SCALA_PKG := aleste
RTLS_DIR := rtl
SIM_DIR := simulation
REPORTS_DIR := reports

# Module Hierarchy (добавлять новые модули здесь)
MODULES := \
	modules/i8255 \
	modules/pwm_dac \
	subsystems/z80 \
	aleste

# Тестовые цели (соответствуют структуре тестов)
TEST_SUITES := \
	modules.i8255_test \
	modules.pwm_dac_test \
	modules.delta_sigma_dac \
	integration.z80_test \
	system.aleste_test

.PHONY: all clean generate test test-unit test-integration test-system wave

all: generate test

# Генерация RTL
generate: $(addprefix gen-,$(MODULES))

gen-%:
	@echo "Generating $*..."
	@mkdir -p $(RTLS_DIR)/$(dir $*)
	@$(SBT) "runMain $(SCALA_PKG).TopLevel $(notdir $*)" > $(RTLS_DIR)/$*.log

# Запуск тестов
test: test-unit test-integration test-system

test-unit:
	@echo "Running unit tests..."
	@$(SBT) "testOnly $(SCALA_PKG).modules.*" | tee $(REPORTS_DIR)/unit_tests.log

test-integration:
	@echo "Running integration tests..."
	@$(SBT) "testOnly $(SCALA_PKG).integration.*" | tee $(REPORTS_DIR)/integration_tests.log

test-system:
	@echo "Running system tests..."
	@$(SBT) "testOnly $(SCALA_PKG).system.*" | tee $(REPORTS_DIR)/system_tests.log

# Анализ waveform
wave:
	@echo "Opening waveforms..."
	@gtkwave $(SIM_DIR)/*.vcd >/dev/null 2>&1 &

# Сборка документации
doc:
	@echo "Generating documentation..."
	@$(SBT) doc
	@cp -r target/scala-2.13/api $(REPORTS_DIR)/scaladoc

# Очистка проекта
clean:
	@echo "Cleaning project..."
	@rm -rf $(RTLS_DIR) $(SIM_DIR) $(REPORTS_DIR) target project/target .bsp .metals

# Инициализация структуры проекта
init:
	@mkdir -p \
		src/main/scala/$(SCALA_PKG)/modules \
		src/main/scala/$(SCALA_PKG)/subsystems \
		src/test/scala/$(SCALA_PKG)/modules \
		src/test/scala/$(SCALA_PKG)/integration \
		src/test/scala/$(SCALA_PKG)/system \
		$(RTLS_DIR)/modules \
		$(RTLS_DIR)/subsystems \
		$(SIM_DIR) \
		$(REPORTS_DIR)
	@touch src/main/scala/$(SCALA_PKG)/main.scala
	@echo "Project structure created"

# Отчеты и метрики
report:
	@echo "Generating reports..."
	@$(SBT) "runMain $(SCALA_PKG).reports.ReportGenerator"
	@open $(REPORTS_DIR)/summary.html  # Для macOS, для Linux: xdg-open

# Синтез (пример для Xilinx)
synth: generate
	@echo "Running synthesis for TopLevel..."
	@vivado -mode batch -source scripts/synth.tcl -log $(REPORTS_DIR)/synth.log


# Yosys/NextPNR для ECP5

# sudo apt install yosys nextpnr-ecp5 trellis-tools
SYNTH_DIR := synth
DEVICE := --um5g-85k --package CABGA381
CONSTRAINTS := constraints.lpf

synth-ecp5: generate
	@echo "Running Yosys synthesis..."
	@mkdir -p $(SYNTH_DIR)
	@yosys -p "read_verilog $(RTLS_DIR)/aleste/*.v; synth_ecp5 -json $(SYNTH_DIR)/$(PROJECT_NAME).json" 2>&1 | tee $(SYNTH_DIR)/yosys.log
	
	@echo "Running NextPNR..."
	@nextpnr-ecp5 $(DEVICE) --json $(SYNTH_DIR)/$(PROJECT_NAME).json --lpf $(CONSTRAINTS) --textcfg $(SYNTH_DIR)/$(PROJECT_NAME)_out.config 2>&1 | tee $(SYNTH_DIR)/nextpnr.log
	
	@echo "Generating bitstream..."
	@ecppack --svf $(SYNTH_DIR)/$(PROJECT_NAME).svf $(SYNTH_DIR)/$(PROJECT_NAME)_out.config $(SYNTH_DIR)/$(PROJECT_NAME).bit 2>&1 | tee $(SYNTH_DIR)/ecppack.log
	
	@echo "Synthesis complete. Bitstream: $(SYNTH_DIR)/$(PROJECT_NAME).bit"

