# ===== BUILD CONFIGURATION =====
PROJECT     := t80_zexall
TOP_MODULE  := T80
SIMULATOR   := verilator
DEVICE      := ecp5-85
LOG_DIR     := logs
REPORT_DIR  := reports
WAVE_DIR    := waveforms
BUILD_DIR   := build
TEST_DIR    := tests/zexall

# ===== TOOLCHAIN PATHS =====
VERILATOR   := verilator
ICARUS      := iverilog
YOSYS       := yosys
NEXTPNR     := nextpnr-ecp5
TRELLIS     := /usr/share/trellis
ECPPROG     := ecpprog

# ===== COLOR DEFINITIONS (ANSI) =====
RED         := \033[0;31m
GREEN       := \033[0;32m
YELLOW      := \033[0;33m
BLUE        := \033[0;34m
MAGENTA     := \033[0;35m
CYAN        := \033[0;36m
RESET       := \033[0m

# ===== SOURCE FILES =====
RTL_SOURCES := $(wildcard cores/t80/*.sv) \
               $(wildcard rtl/*.sv)

TEST_SOURCES := tb/t80/zexall/tb_$(PROJECT).cpp

# ===== SIMULATION TARGETS =====
.PHONY: all sim verilator icarus clean help

all: sim

# === SIMULATION ===
sim: $(SIMULATOR)

verilator: $(BUILD_DIR)/verilator/$(PROJECT)
	@echo "$(GREEN)Running Verilator simulation...$(RESET)"
	@$(BUILD_DIR)/verilator/$(PROJECT) | tee $(LOG_DIR)/verilator.log

icarus: $(BUILD_DIR)/icarus/$(PROJECT)
	@echo "$(GREEN)Running Icarus simulation...$(RESET)"
	@vvp $(BUILD_DIR)/icarus/$(PROJECT) | tee $(LOG_DIR)/icarus.log

# === BUILD RULES ===
$(BUILD_DIR)/verilator/$(PROJECT): $(RTL_SOURCES) $(TEST_SOURCES)
	@echo "$(BLUE)Building with Verilator...$(RESET)"
	@mkdir -p $(BUILD_DIR)/verilator $(LOG_DIR)
	@$(VERILATOR) -Wall --cc --exe --build \
		--top-module $(TOP_MODULE) \
		--Mdir $(BUILD_DIR)/verilator \
		--trace \
		-Icores/t80 \
		-Irtl \
		$(RTL_SOURCES) \
		$(TEST_SOURCES) \
		-o $(PROJECT) \
		| tee $(LOG_DIR)/verilator_build.log

$(BUILD_DIR)/icarus/$(PROJECT): $(RTL_SOURCES) tb/t80/zexall/tb_$(PROJECT).sv
	@echo "$(BLUE)Building with Icarus...$(RESET)"
	@mkdir -p $(BUILD_DIR)/icarus $(LOG_DIR)
	@$(ICARUS) -g2012 \
		-Icores/t80 \
		-Irtl \
		-o $@ \
		$(RTL_SOURCES) \
		tb/t80/zexall/tb_$(PROJECT).sv \
		| tee $(LOG_DIR)/icarus_build.log

# === FPGA SYNTHESIS ===
# === ECP5 SYNTHESIS FLOW ===
fpga: $(BUILD_DIR)/$(PROJECT).bit

$(BUILD_DIR)/$(PROJECT).json: $(RTL_SOURCES)
	@echo "$(MAGENTA)Running Yosys synthesis...$(RESET)"
	@mkdir -p $(BUILD_DIR) $(REPORT_DIR)
	@$(YOSYS) -q -l $(LOG_DIR)/yosys.log -p \
		"read_verilog -sv $(RTL_SOURCES); \
		synth_ecp5 -top $(TOP_MODULE) -json $@ -report $(REPORT_DIR)/yosys.rpt"

$(BUILD_DIR)/$(PROJECT).config: $(BUILD_DIR)/$(PROJECT).json
	@echo "$(MAGENTA)Running nextpnr...$(RESET)"
	@$(NEXTPNR) --$(DEVICE) \
		--json $< \
		--textcfg $@ \
		--lpf $(TRELLIS)/../prjtrellis/misc/ecp5.lpf \
		--log $(LOG_DIR)/nextpnr.log \
		--report $(REPORT_DIR)/nextpnr.rpt

$(BUILD_DIR)/$(PROJECT).bit: $(BUILD_DIR)/$(PROJECT).config
	@echo "$(MAGENTA)Generating bitstream...$(RESET)"
	@ecppack --svf $(BUILD_DIR)/$(PROJECT).svf $< $@ \
		| tee $(LOG_DIR)/ecppack.log

# === UTILITY TARGETS ===
# === CLEANUP ===
clean:
	@echo "$(RED)Cleaning build artifacts...$(RESET)"
	@rm -rf $(BUILD_DIR) $(LOG_DIR) $(REPORT_DIR) $(WAVE_DIR)
	@find . -name "*.vcd" -delete

# === WAVEFORM VIEWING ===
wave:
	@echo "$(CYAN)Opening waveforms...$(RESET)"
	@gtkwave $(WAVE_DIR)/$(PROJECT).vcd &

# === HELP ===
help:
	@echo "$(YELLOW)=== AVAILABLE TARGETS ===$(RESET)"
	@echo "$(GREEN)sim$(RESET)       : Run default simulation (Verilator)"
	@echo "$(GREEN)verilator$(RESET) : Build and run Verilator simulation"
	@echo "$(GREEN)icarus$(RESET)    : Build and run Icarus simulation"
	@echo "$(GREEN)fpga$(RESET)      : Synthesize for ECP5 FPGA"
	@echo "$(GREEN)wave$(RESET)      : View waveforms (GTKWave)"
	@echo "$(GREEN)clean$(RESET)     : Remove all build artifacts"
	@echo "$(GREEN)help$(RESET)      : Show this help message"

# === INITIALIZATION ===
init:
	@echo "$(YELLOW)Initializing project directories...$(RESET)"
	@mkdir -p $(BUILD_DIR) $(LOG_DIR) $(REPORT_DIR) $(WAVE_DIR)

# === TEST FILE PREPARATION ===
testfiles:
	@echo "$(YELLOW)Preparing test files...$(RESET)"
	@make -C $(TEST_DIR)

# === DEPENDENCY CHECK ===
check-deps:
	@echo "$(YELLOW)=== CHECKING TOOL DEPENDENCIES ===$(RESET)"
	@which $(VERILATOR) >/dev/null || echo "$(RED)Verilator not found!$(RESET)"
	@which $(ICARUS) >/dev/null || echo "$(RED)Icarus Verilog not found!$(RESET)"
	@which $(YOSYS) >/dev/null || echo "$(RED)Yosys not found!$(RESET)"
	@which $(NEXTPNR) >/dev/null || echo "$(RED)nextpnr not found!$(RESET)"
	@which $(ECPPROG) >/dev/null || echo "$(RED)ecpprog not found!$(RESET)"
	@echo "$(GREEN)Dependency check complete$(RESET)"