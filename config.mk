# Verilator flags
VERILATOR_FLAGS := \
    -Wno-fatal \
    -Wno-UNUSEDSIGNAL \
    -Wno-IMPLICIT

# Yosys synthesis options
SYNTH_FLAGS := synth_ecp5 -abc9