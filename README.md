# Aleste LX FPGA

A modern FPGA implementation of the classic Aleste computer architecture.

This project represents an ongoing effort to recreate and reimagine the Aleste system using contemporary FPGA technology. While development continues, there is no guarantee of final completion - it remains an experimental platform for exploring these classic computing concepts.

Key aspects:

- Faithful recreation of the original Aleste architecture
- Modern FPGA implementation
- Experimental nature (may not reach full completion)
- Platform for technical exploration

The project serves primarily as:

- A technical proof-of-concept
- An educational platform for retro computing
- A testbed for architectural experimentation

Note: This remains a work in progress with no definitive completion timeline.

project/
├── cores/
│   ├── t80/                  # T80 Core
│   │   ├── T80.vhd           # Main module
│   │   ├── T80_ALU.vhd
│   │   ├── T80_MCode.vhd
│   │   ├── T80_Pack.vhd
│   │   └── T80_Reg.vhd
│   └── peripherals/          # Peripherals (if added)
│       └── i8255.sv          # Example peripheral device
├── rtl/                      # Common RTL modules
│   ├── ram.sv                # Memory module
│   ├── system_bus.sv         # System bus
│   └── clock_gen.sv          # Clock generator
├── tb/
│   ├── t80/                  # T80 testbenches
│   │   ├── basic/            # Basic tests
│   │   └── zexall/           # ZEXALL tests
│   └── common/               # Common testbench utilities
├── tests/
│   └── zexall/
│       ├── zexall.com
│       ├── zexdoc.com
│       └── Makefile          # For preparing test files
├── include/                  # Common header files
│   └── t80_defines.svh       # Common definitions
├── scripts/                  # Build/run scripts
├── waveforms/                # Waveform dumps
├── Makefile                  # Main Makefile