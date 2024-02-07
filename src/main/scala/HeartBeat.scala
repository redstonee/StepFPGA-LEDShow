package redstone.cv

import chisel3._

/**
 * Module for mode 0:
 * A heart beat signal generator
 *
 * Input clock frequency: 2Hz
 */
class HeartBeat extends Blinker {
  private val ledReg = RegInit(0.U(1.W)) // 1 bit register for led

  when(clockCnt < realPeriod * 2.U - 1.U) {
    clockCnt := clockCnt + 1.U
    when(clockCnt === realPeriod - 1.U) { // half period
      ledReg := !ledReg
    }
  }.otherwise {
    clockCnt := 0.U
    currentLed := currentLed + 1.U
    ledReg := !ledReg
  }

  io.out := ledReg << currentLed
}
