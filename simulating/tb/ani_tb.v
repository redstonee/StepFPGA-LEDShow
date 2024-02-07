`timescale 1ns/1ps

module ani_tb;
    reg clk;
    reg rst;
    wire[5:0] seg;
    
    always begin
        clk = 1;
        #10 ;
        clk = 0;
        #10 ;
    end

    initial
    begin        
        $dumpfile("wave_ani.vcd");   
        $dumpvars(0, ani_tb);    
    end

    initial begin
        rst = 1'b1;
        #10;
        rst = 1'b0;
    end

    SegDisplayAnimation sda(
        .clock(clk),
        .reset(rst),
        .io_segAtoF(seg)
    );


    always begin
        #100;
        if ($time >= 1000)
            $finish ;
    end

endmodule
