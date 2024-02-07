`timescale 1ns/1ps

module dec_tb;
    reg[1:0] d1;
    reg[1:0] d2;
    wire[6:0] seg1;
    wire[6:0] seg2;
    
    initial begin
        d1 = 0;
        d2 = 0;
    end

    always begin
        d1 = d1 + 1;
        #1 ;
        d2 = d2 + 1;
        #1 ;
    end

    initial
    begin        
        $dumpfile("wave_dec.vcd");   
        $dumpvars(0, dec_tb);    
    end

    SegDecoder dec(
        .io_data1(d1),
        .io_data2(d2),
        .io_seg1(seg1),
        .io_seg2(seg2)
    );


    always begin
        #1 ;
        if ($time >= 10)
            $finish ;
    end

endmodule
