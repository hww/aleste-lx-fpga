cat > tmp/minimal_tb.vhd << 'EOF'
library ieee;
use ieee.std_logic_1164.all;

entity tb is
end entity tb;

architecture sim of tb is
begin
  assert false report "Simulation started successfully" severity note;
end architecture;
EOF

ghdl -a tmp/minimal_tb.vhd
ghdl -e tb
ghdl -r tb
