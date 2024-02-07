`timescale 1us/1ns

module fo_tb;
    reg clk;
    reg rst;
    reg[2:0] realPeriod;
    wire out;
    
    real CYCLE_100kHz = 10;

    initial begin
        realPeriod = 3'b001;
        rst = 0;
    end

    always begin
        clk = 0;
        #(CYCLE_100kHz/2);
        clk = 1;
        #(CYCLE_100kHz/2);
    end

    always begin
        rst = 1;
        #CYCLE_100kHz ;
        rst = 0;
        #(CYCLE_100kHz*50_000) ;
        realPeriod = realPeriod + 1;
    end

    initial begin        
        $dumpfile("wave_fo.vcd");   
        $dumpvars(0, fo_tb);    
    end


    FadeOut fo(
        .clock(clk),
        .reset(rst),
        .io_realPeriod(realPeriod),
        .io_out(out)
    );


    always begin
        #100;
        if ($time >= CYCLE_100kHz*2_000_000)
            $finish ;
    end

endmodule
