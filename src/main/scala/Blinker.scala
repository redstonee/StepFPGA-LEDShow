package redstone.cv

import chisel3._

/**
 * This is a template for creating a new blinker module which merges the common parts of them
 *
 * Create a new module by extending this class
 */
abstract class Blinker extends Module {
  val io = IO(new Bundle {
    val period = Input(UInt(2.W)) // 0 ~ 3 for 1 ~ 4 seconds
    val out = Output(UInt(8.W))
  })
  val realPeriod = Wire(UInt(3.W))
  realPeriod := (0.U ## io.period) + 1.U

  val clockCnt = RegInit(0.U(20.W)) //register for counting
  val currentLed = RegInit(0.U(3.W)) // 3 bit register for current led
}
