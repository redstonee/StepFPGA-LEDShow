`timescale 1ms/1ns

module hb_tb;
    reg clk;
    reg rst;
    reg[1:0] T;
    wire[7:0] leds;
    
    real CYCLE_2Hz = 500;

    always begin
        clk = 0;
        #(CYCLE_2Hz/2);
        clk = 1;
        #(CYCLE_2Hz/2);
    end

    initial
    begin            
        $dumpfile("wave_hb.vcd");
        $dumpvars(0, hb_tb);
    end

    initial begin
        T = 2'b0;
        rst = 1'b1;
        #CYCLE_2Hz;
        rst = 1'b0;
    end

    always begin
        #(CYCLE_2Hz*20);
        T = T + 1;
    end

    HeartBeat hb(
        .clock(clk),
        .reset(rst),
        .io_period(T),
        .io_out(leds)
    );


    always begin
        #100;
        if ($time >= 100000)
            $finish ;
    end

endmodule
