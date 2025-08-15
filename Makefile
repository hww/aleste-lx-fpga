# ===== PROJECT CONFIG =====
PROJECT := aleste
ROOT_DIR := $(shell pwd)
CONFIG_FILE ?= config.mk

# ===== INCLUDE CONFIG =====
include $(CONFIG_FILE)

# ===== TOOLCHAIN =====
VERILATOR := verilator
YOSYS := yosys
GTKWAVE := gtkwave

# ===== DIR STRUCTURE =====
RTL_DIR := $(ROOT_DIR)/rtl
TB_DIR := $(ROOT_DIR)/tb
BUILD_DIR := $(ROOT_DIR)/build
SYNTH_DIR := $(BUILD_DIR)/synth
SIM_DIR := $(BUILD_DIR)/sim
UNIT_DIR := $(BUILD_DIR)/unit
REPORT_DIR := $(BUILD_DIR)/reports

# ===== MODULE DISCOVERY =====
MODULES := $(notdir $(wildcard $(RTL_DIR)/*))
TBENCHES := $(patsubst tb_%,%,$(basename $(notdir $(wildcard $(TB_DIR)/tb_*.cpp))))

# ===== BUILD RULES =====
.PHONY: all test-all test-unit synth clean help

all: test-all synth

# === UNIT TEST ===
define UNIT_TEST_RULE
test-unit-$(1): $(UNIT_DIR)/$(1)/sim
	@echo "=== Running unit test: $(1) ==="
	$$< | tee $(REPORT_DIR)/unit_$(1).log

$(UNIT_DIR)/$(1)/sim: $(TB_DIR)/tb_$(1).cpp $(RTL_DIR)/$(1)/*.sv
	@echo "=== Building unit test: $(1) ==="
	@mkdir -p $$(@D)
	$(VERILATOR) --cc --exe --build \
		--top-module $(1) \
		--Mdir $$(@D)/obj \
		--trace \
		-I$(RTL_DIR)/$(1) \
		$$^ -o $$@
endef

$(foreach mod,$(MODULES),$(eval $(call UNIT_TEST_RULE,$(mod))))

# === SYSTEM TEST ===
test-all: $(addprefix test-unit-,$(MODULES))
	@echo "=== All tests completed ==="

# === SYNTHESIS ===
synth: $(addprefix $(SYNTH_DIR)/,$(addsuffix .json,$(MODULES)))

$(SYNTH_DIR)/%.json: $(RTL_DIR)/%/*.sv
	@echo "=== Synthesizing module: $* ==="
	@mkdir -p $(@D)
	$(YOSYS) -q -l $(REPORT_DIR)/$*_synth.log \
		-p "read_verilog -sv $$^; synth_ecp5 -top $* -json $$@"

# === WAVEFORM VIEWING ===
wave-%: $(UNIT_DIR)/%/dump.vcd
	$(GTKWAVE) $$< &

# === UTILITIES ===
clean:
	rm -rf $(BUILD_DIR)

list-modules:
	@echo "Available modules: $(MODULES)"
	@echo "Available tests: $(TBENCHES)"

help:
	@echo "=== Professional HDL Test System ==="
	@echo "make all               - Run all tests and synthesis"
	@echo "make test-all          - Run all unit tests"
	@echo "make test-unit-<mod>   - Run specific unit test"
	@echo "make synth            - Synthesize all modules"
	@echo "make wave-<mod>       - View waveforms for module"
	@echo "make clean            - Clean build artifacts"
	@echo "make list-modules     - List available modules"