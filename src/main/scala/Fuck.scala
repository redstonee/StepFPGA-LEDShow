package redstone.cv

import chisel3._
import circt.stage.ChiselStage


class LedShow extends Module {
  val io = IO(new Bundle {
    val buttons = Input(UInt(4.W)) // For mode selection
    val switches = Input(UInt(4.W)) // For speed selection
    val leds = Output(UInt(8.W))
    val seg1 = Output(UInt(7.W))
    val seg2 = Output(UInt(7.W))
    val dig = Output(UInt(2.W))
    val rgb1 = Output(UInt(3.W))
    val rgb2 = Output(UInt(3.W))
  })

  private val clock100kHz = Module(new ClockDivider(12e6.toInt, 1e5.toInt)) // 12MHz -> 100kHz
  private val clock2Hz = Module(new ClockDivider(1e5.toInt, 2)) // 100kHz -> 2Hz
  clock100kHz.clock := clock
  clock2Hz.clock := clock100kHz.io.clkOut

  private val modeReg = RegInit(0.U(2.W)) // Blinking mode
  modeReg :=
    Mux(!io.buttons(0), 0.U,
      Mux(!io.buttons(1), 1.U,
        Mux(!io.buttons(2), 2.U,
          Mux(!io.buttons(3), 3.U, modeReg))))

  private val blinkPeriod = // Period - 1s
    Mux(io.switches(0), 0.U,
      Mux(io.switches(1), 1.U,
        Mux(io.switches(2), 2.U, 3.U)))
  private val periodValid = (io.switches === "b1000".U) || (io.switches === "b0100".U) || (io.switches === "b0010".U) || (io.switches === "b0001".U)

  private val segDecoder = Module(new SegDecoder) // 7-segment decoder
  private val segAnimation = Module(new SegDisplayAnimation) // PPlay a simple animation when the switches are invalid
  segDecoder.io.data1 := modeReg
  segDecoder.io.data2 := blinkPeriod
  segAnimation.clock := clock2Hz.io.clkOut

  io.seg1 := segDecoder.io.seg1 //Showing the mode
  io.seg2 := Mux(periodValid, segDecoder.io.seg2, segAnimation.io.segAtoF ## 0.U(1.W)) //Showing the period or animation
  io.dig := 0.U // Always enable the displays

  private val heartBeatModule = Module(new HeartBeat) // Blinking Mode 0: Heartbeat
  heartBeatModule.clock := clock2Hz.io.clkOut
  heartBeatModule.io.period := blinkPeriod
  heartBeatModule.reset := ~io.buttons(0)

  private val breatheModule = Module(new Breath) // Blinking Mode 1: Breathe
  breatheModule.clock := clock100kHz.io.clkOut
  breatheModule.io.period := blinkPeriod
  breatheModule.reset := ~io.buttons(1)

  private val waterFlowModule = Module(new WaterFlow) // Blinking Mode 2, 3: Water-flow
  waterFlowModule.clock := clock100kHz.io.clkOut
  waterFlowModule.io.period := blinkPeriod
  waterFlowModule.io.bidirectional := modeReg(0) // Mode 2: Mono-directional, Mode 3: Bidirectional
  waterFlowModule.reset := ~io.buttons(2)


  private val blinkingModulesOut = VecInit(heartBeatModule.io.out, breatheModule.io.out,
    waterFlowModule.io.out, waterFlowModule.io.out)
  io.leds := ~Mux(periodValid, blinkingModulesOut(modeReg), 0.U) // Active low, only play it when the switches are valid

  // The RGB LEDs are temporarily unused, so just turn them off
  private val rgbModule1 = Module(new RGBController)
  private val rgbModule2 = Module(new RGBController)
  rgbModule1.io.r_val := 0.U
  rgbModule1.io.g_val := 0.U
  rgbModule1.io.b_val := 0.U
  rgbModule2.io.r_val := 0.U
  rgbModule2.io.g_val := 0.U
  rgbModule2.io.b_val := 0.U

  io.rgb1 := ~rgbModule1.io.out // Active low
  io.rgb2 := ~rgbModule2.io.out // Active low
}

object Fuck extends App {
  ChiselStage.emitSystemVerilogFile(new LedShow,
    firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info", "--split-verilog", "-o=build",
      "--lowering-options=disallowLocalVariables,disallowPackedArrays,locationInfoStyle=wrapInAtSquareBracket,noAlwaysComb"))

}
