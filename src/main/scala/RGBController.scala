package redstone.cv

import chisel3._
import chisel3.util.Cat

/**
 * An RGB controller
 *
 * Input 3 duty cycles for R, G, B, ranging from 0 to 100
 *
 * Output 3 PWM signal with a duty cycle specified by the input
 *
 * The output frequency is 1/100 of the input frequency
 */
class RGBController extends Module {
  val io = IO(new Bundle {
    val r_val = Input(UInt(7.W))
    val g_val = Input(UInt(7.W))
    val b_val = Input(UInt(7.W))
    val out = Output(UInt(3.W))
  })

  private val pwm_r = Module(new PWMGenerator)
  private val pwm_g = Module(new PWMGenerator)
  private val pwm_b = Module(new PWMGenerator)

  pwm_r.io.duty := io.r_val
  pwm_g.io.duty := io.g_val
  pwm_b.io.duty := io.b_val

  io.out := Cat(pwm_r.io.pwmOut, pwm_g.io.pwmOut, pwm_b.io.pwmOut)

}
