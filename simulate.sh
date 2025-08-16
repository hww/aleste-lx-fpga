#!/bin/bash
set -euo pipefail
set -x

GHDL_ARGS='-fexplicit --work=work  --ieee=synopsys  --std=08'
#ROOT_DIR=$(pwd)
#echo $ROOT_DIR
## 1. Готовим временную папку
#WORK_DIR=$(mktemp -d)
#trap "rm -rf '$WORK_DIR'" EXIT
ROOT_DIR=$(pwd)
WORK_DIR=$(pwd)
# 2. Компилируем исходники напрямую из исходной папки
#cd "$WORK_DIR"

# 1. Очистка предыдущей компиляции
rm -f *.o *.cf

# 2. Компиляция исходников T80 в правильном порядке
FILES=(
  "T80_Pack.vhd"
  "T80_MCode.vhd"
  "T80_ALU.vhd"
  "T80_Reg.vhd"
  "T80.vhd"
  "T80se.vhd"
)

for file in "${FILES[@]}"; do
  echo "Компиляция $file..."
  ghdl -a $GHDL_ARGS "${ROOT_DIR}/rtl/cores/t80/$file"
done

echo "=== MAKE TESTBECH =="

# 3. Создание тестбенча с правильными портами
cat > tb.vhd << 'EOF'
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity tb is
end entity;

architecture sim of tb is
  signal clk     : std_logic := '0';
  signal reset_n : std_logic := '0';
  signal data_in : std_logic_vector(7 downto 0) := x"00";
begin
  -- Инициализация всех входных портов
  dut: entity work.t80se
    port map(
      CLK_n    => clk,
      CLKEN    => '1',
      RESET_n  => reset_n,
      WAIT_n   => '1',
      INT_n    => '1',
      NMI_n    => '1',
      BUSRQ_n  => '1',
      M1_n     => open,
      MREQ_n   => open,
      IORQ_n   => open,
      RD_n     => open,
      WR_n     => open,
      RFSH_n   => open,
      HALT_n   => open,
      BUSAK_n  => open,
      A        => open,
      DI       => data_in,
      DO       => open
    );

  -- Тактовый сигнал (10 нс период)
  clk <= not clk after 5 ns;

  -- Процесс сброса и инициализации
  process
  begin
    -- Начальный сброс (40 нс)
    reset_n <= '0';
    wait for 40 ns;
    
    -- Снятие сброса
    reset_n <= '1';
    
    -- Подача тестовых данных
    data_in <= x"F0";
    wait for 20 ns;
    data_in <= x"0F";
    
    wait;
  end process;
end architecture;
EOF

# 4. Компиляция тестбенча
echo "=== COMPILE TB =="
ghdl -a ${GHDL_ARGS} tb.vhd

# 5. Создание исполняемого файла
echo "=== MAKE TB =="
ghdl -e  ${GHDL_ARGS} tb   

echo "=== DIRS =="
echo ghdl dir
ghdl dir --std=08

echo "=== SIMULATION =="

# 6. Запуск симуляции
# Внимание порядок аргументов важен TB должно быть после --std=08  --ieee=synopsys 
ghdl -r  --std=08  --ieee=synopsys  tb --wave="wave.ghw" --stop-time=100ns   2>&1 | grep -v "metavalue detected" | grep -v "result will be 'X'"

