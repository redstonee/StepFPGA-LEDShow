package redstone.cv

import chisel3._

/**
 * PWM module
 *
 * Output a PWM signal with a duty cycle specified by the input
 *
 * The output frequency is 1/100 of the input frequency
 */
class PWMGenerator extends Module {
  val io = IO(new Bundle {
    val duty = Input(UInt(7.W)) // 0-100
    val pwmOut = Output(Bool())
  })

  private val counter = RegInit(0.U(7.W))

  counter := counter + 1.U
  when(counter >= 100.U) {
    counter := 0.U
  }

  io.pwmOut := (counter < io.duty)
}

