`timescale 1us/1ns

module pwm_tb;
    reg clk;
    reg rst;
    reg[6:0] duty;
    wire out;
    
    real CYCLE_100kHz = 10;

    always begin
        clk = 1;
        #(CYCLE_100kHz/2);
        clk = 0;
        #(CYCLE_100kHz/2);
    end

    initial
    begin            
        $dumpfile("wave_pwm.vcd");
        $dumpvars(0, pwm_tb);
    end

    initial begin
        duty = 2'b0;
        rst = 1'b1;
        #(CYCLE_100kHz*2);
        rst = 1'b0;
    end

    always begin
        #(CYCLE_100kHz*200);
        duty = duty + 1;
    end

    PWMGenerator pg(
        .clock(clk),
        .reset(rst),
        .io_duty(duty),
        .io_pwmOut(out)
    );


    always begin
        #100;
        if ($time >= 200_000)
            $finish ;
    end

endmodule
