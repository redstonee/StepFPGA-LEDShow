`timescale 1ns/1ps

module clock100k_tb;
    reg clkIn;
    reg rst;
    wire clkOut;
    
    always begin
        clkIn = 1;
        #1 ;
        clkIn = 0;
        #1 ;
    end

    initial
    begin        
        $dumpfile("wave_clock100k.vcd");   
        $dumpvars(0, clock100k_tb);    
    end

    initial begin
        rst = 1'b1;
        #10;
        rst = 1'b0;
    end

    ClockDivider clk100k(
        .clock(clkIn),
        .reset(rst),
        .io_clkOut(clkOut)
    );


    always begin
        #100;
        if ($time >= 1000)
            $finish ;
    end

endmodule
