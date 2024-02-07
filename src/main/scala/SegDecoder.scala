package redstone.cv

import chisel3._

/**
 * Segmentation decoder for 2 digits:
 *
 * Dual 2-bit input for 0-3 -> 7-bit output for displaying 1-4
 */
class SegDecoder extends Module {
  val io = IO(new Bundle {
    val data1 = Input(UInt(2.W))
    val data2 = Input(UInt(2.W))
    val seg1 = Output(UInt(7.W))
    val seg2 = Output(UInt(7.W))
  })

  // Define the lookup table for the 7-segment display
  private val lookupTable = VecInit(
    "b0110000".U, // 1
    "b1101101".U, // 2
    "b1111001".U, // 3
    "b0110011".U, // 4
  )

  io.seg1 := lookupTable(io.data1)
  io.seg2 := lookupTable(io.data2)
}

