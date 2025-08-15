`timescale 1ns / 1ps

module i8255 (
  input  logic        clk,      // Тактовый сигнал
  input  logic        reset_n,  // Асинхронный сброс (активный 0)
  input  logic        cs_n,     // Chip Select (активный 0)
  input  logic        rd_n,     // Read Strobe (активный 0)
  input  logic        wr_n,     // Write Strobe (активный 0)
  input  logic [1:0]  addr,     // Адрес порта (A1:A0)
  inout  wire  [7:0]  data      // Шина данных (тристат)
);

  // Регистры портов
  logic [7:0] port_a, port_b, port_c;
  logic [7:0] control_reg;

  // Тристат для data bus
  assign data = (!cs_n && !rd_n) ? 
    (addr == 2'b00) ? port_a :
    (addr == 2'b01) ? port_b :
    (addr == 2'b10) ? port_c :
    (addr == 2'b11) ? control_reg : 8'hZZ : 8'hZZ;

  // Логика записи
  always_ff @(posedge clk or negedge reset_n) begin
    if (!reset_n) begin
      port_a <= 8'h00;
      port_b <= 8'h00;
      port_c <= 8'h00;
      control_reg <= 8'h9B;  // Режим 0 (все порты на вывод)
    end else if (!cs_n && !wr_n) begin
      case (addr)
        2'b00: port_a <= data;
        2'b01: port_b <= data;
        2'b10: port_c <= data;
        2'b11: control_reg <= data;
      endcase
    end
  end

endmodule
