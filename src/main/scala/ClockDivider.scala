package redstone.cv

import chisel3._
import chisel3.util.log2Ceil

/**
 * A clock divider generator:
 * Output a clock with a frequency specified by the parameters
 *
 * (Fout / Fin) must be an even number
 *
 * @param inputFreq Input clock frequency
 * @param outputFreq Output clock frequency
 *
 */
class ClockDivider(val inputFreq: Int, val outputFreq: Int) extends Module {
  val io = IO(new Bundle {
    val clkOut = Output(Clock())
  })
  println()
  private val divider = RegInit(0.U(log2Ceil(inputFreq / outputFreq).W))
  private val clkReg = RegInit(false.B)

  divider := divider + 1.U
  when(divider >= (inputFreq / outputFreq / 2).U) {
    divider := 0.U
    clkReg := ~clkReg
  }

  io.clkOut := clkReg.asClock

}
