package redstone.cv

import chisel3._

/**
 * Module for mode 1:
 * Breathing LED signal generator
 *
 * Output a PWM signal with a duty cycle that changes from 0 to 100 and back to 0 for every LED
 *
 * Input clock frequency is 100kHz
 */
class Breath extends Blinker {

  private val pwmDuty = RegInit(1.U(7.W))
  private val increaseDuty = RegInit(true.B) // A flag for duty cycle change direction

  when(clockCnt < realPeriod * 500.U - 1.U) { // For duty cycle change, change the duty every 1/200 of the period
    clockCnt := clockCnt + 1.U
  }.otherwise {
    clockCnt := 0.U
    pwmDuty := Mux(increaseDuty, pwmDuty + 1.U, pwmDuty - 1.U)
    when(pwmDuty === 100.U) {
      increaseDuty := false.B
    }.elsewhen(pwmDuty === 1.U) {
      increaseDuty := true.B
    }.elsewhen(pwmDuty === 0.U) {
      currentLed := currentLed + 1.U
    }
  }

  private val pwmGenerator = Module(new PWMGenerator)
  pwmGenerator.clock := clock
  pwmGenerator.io.duty := pwmDuty
  io.out := pwmGenerator.io.pwmOut << currentLed
}
