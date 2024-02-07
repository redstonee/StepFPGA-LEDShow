package redstone.cv

import chisel3._

/**
 * Module for displaying a simple animation on the 7-segment display
 *
 * Input clock frequency: 2Hz
 */
class SegDisplayAnimation extends Module {
  val io = IO(new Bundle {
    val segAtoF = Output(UInt(6.W))
  })
  private val segReg = RegInit(0.U(6.W))

  segReg := Mux(segReg <= 1.U, 32.U, segReg >> 1.U)

  io.segAtoF := segReg
}
