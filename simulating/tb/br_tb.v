`timescale 1us/1ns

module br_tb;
    reg clk;
    reg rst;
    reg[1:0] T;
    wire[7:0] leds;
    
    real CYCLE_100kHz = 10;

    always begin
        clk = 0;
        #(CYCLE_100kHz/2);
        clk = 1;
        #(CYCLE_100kHz/2);
    end

    initial
    begin            
        $dumpfile("wave_br.vcd");
        $dumpvars(0, br_tb);
    end

    initial begin
        T = 2'b0;
        rst = 1'b1;
        #CYCLE_100kHz;
        rst = 1'b0;
    end

    always begin
        #(CYCLE_100kHz * 4_000_000);
        T = T + 1;
    end

    Breath br(
        .clock(clk),
        .reset(rst),
        .io_period(T),
        .io_out(leds)
    );


    always begin
        #1000;
        if ($time >= 2_000_000 * CYCLE_100kHz)
            $finish ;
    end

endmodule
