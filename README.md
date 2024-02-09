# StepFPGA-LEDShow

## Introduction

This is a simple project for [`2024 Winter Break Event`](https://www.eetree.cn/activity/15) by `EETree` which 
controls LEDs using the Step-MXO2-LPC FPGA board.  
Below is the peripheral configuration of the Step-MXO2-LPC FPGA board.

* 8 LEDs
* 4 buttons
* 4 switches
* 2 7-segment displays
* a 12MHz crystal oscillator
* 2 RGB LEDs (not used in this project)

The project is written in Chisel and the generated Verilog code is synthesized and implemented by the Lattice Diamond
software or [StepFPGA's WebIDE](https://www.stepfpga.com/project). In fact, it should be able to be synthesized and
implemented for any FPGA board with those peripherals.  
The LED will be lit up in 4 different patterns:

1. Heartbeat: The LED will be lit up in a heartbeat pattern one by one and over again.
2. Breathing: The LED will be lit up in a breathing pattern one by one and over again.
3. Mono-directional water-flow: The LED will be lit up in a water-flow pattern one by one and fade out one and over
   again.
4. Bidirectional water-flow: The LED will be lit up in a water-flow pattern from both ends to the other ends and fade
   out and over again.  
   The patterns can be selected by the buttons on the FPGA board and the speed of the patterns can be adjusted by the
   switches on the board.

## Build

* Clone the repository
* Install [sbt](https://www.scala-sbt.org/download.html)
* Run `sbt run` in the root directory of the repository
* The Verilog code will be generated in the `build` directory
* Open the Verilog code in the Lattice Diamond software or [StepFPGA's WebIDE](https://www.stepfpga.com/project) and
  synthesize and implement it
* Program the FPGA board with the generated bitstream file

## Simulation

There are a few simple test benches for each module in the [tb](simulating/tb) directory. You can simulate the modules
with [Icarus Verilog](https://github.com/steveicarus/iverilog) and [GTKWave](https://github.com/gtkwave/gtkwave).  
