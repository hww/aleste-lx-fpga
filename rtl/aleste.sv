// Название файла: rtl/i8255.sv
`timescale 1ns / 1ps

module aleste (
  input  logic        clk,
  input  logic        reset_n,
  inout  wire  [7:0]  data_bus,
  // ... остальные порты
);

  // Логика модуля
  logic [7:0] reg_port_a;

  always_ff @(posedge clk or negedge reset_n) begin
    if (!reset_n) begin
      reg_port_a <= '0;
    end else begin
      // ... логика
    end
  end

endmodule
