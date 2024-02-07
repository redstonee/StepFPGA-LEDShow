`timescale 1us/1ns

module wf_tb;
    reg clk;
    reg rst;
    parameter bidir = 1'b1; // Selects bidirectional or mono-directional mode
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
        $dumpfile("wave_wf.vcd");
        $dumpvars(0, wf_tb);
    end

    initial begin
        T = 0;
        rst = 1;
        #CYCLE_100kHz;
        rst = 0;
    end

    always begin
        #(CYCLE_100kHz*400_000);
        T = T + 1;
    end

    WaterFlow wf(
        .clock(clk),
        .reset(rst),
        .io_period(T),
        .io_bidirectional(bidir),
        .io_out(leds)
    );


    always begin
        #1000;
        if ($time >= (CYCLE_100kHz*2_000_000))
            $finish ;
    end

endmodule
