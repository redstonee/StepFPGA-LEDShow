package redstone.cv

import chisel3._
import chisel3.util.{Cat, is, switch}

import java.util

/**
 * Fade-out signal generator
 *
 * Output a PWM signal with a duty cycle that decreases from 100% to 0% in half a period specified by the parameter
 *
 * Input clock frequency: 100kHz
 */
class FadeOut extends Module {
  val io = IO(new Bundle {
    val realPeriod = Input(UInt(3.W)) // 1 ~ 4 seconds
    val out = Output(Bool())
  })

  val pwmDuty = RegInit(100.U(7.W))
  private val clockCnt = RegInit(0.U(12.W)) // For duty cycle change, the longest switching time is 20ms, which is 2000 clock cycles

  when(clockCnt < io.realPeriod * 500.U - 1.U) {
    clockCnt := clockCnt + 1.U
  }.otherwise { // Change the duty every 1/100 of the period
    clockCnt := 0.U
    pwmDuty := Mux(pwmDuty > 0.U, pwmDuty - 1.U, 0.U)
  }

  private val pwmGen = Module(new PWMGenerator)
  pwmGen.io.duty := pwmDuty
  io.out := pwmGen.io.pwmOut
}

/**
 * Module for mode 2 and 3:
 * Water-flow with fade-out
 *
 * Can be either mono-directional or bi-directional
 *
 * Input clock frequency: 100kHz
 */

class WaterFlow extends Module {
  val io = IO(new Bundle {
    val period = Input(UInt(2.W)) // 1 ~ 4 seconds
    val bidirectional = Input(Bool())
    val out = Output(UInt(8.W))
  })

  val realPeriod = Wire(UInt(3.W))
  realPeriod := (0.U ## io.period) + 1.U

  private val fadeOutMods = List.fill(8)(Module(new FadeOut))
  private val syncReg = RegInit(0.U(8.W)) // Synchronize the fade-out signals by resetting them

  for (i <- 0 until 8) {
    fadeOutMods(i).io.realPeriod := realPeriod
    fadeOutMods(i).reset := syncReg(i)
  }

  private val toSync = RegInit(false.B) // A flag for synchronization

  private val currentLed = RegInit(0.U(3.W)) // 3 bit register for current led in mono-directional mode
  private val led2LightUp = RegInit("b10000001".U(8.W)) // 8 bit register for led to be lit in bidirectional mode
  private val direction = RegInit(1.B) // 1: inner, 0: outer

  private val clockCnt = RegInit(0.U(17.W)) // Each clock cycle is 10us, the longest switching time is 0.5s, which is 50000 clock cycles


  when(clockCnt < realPeriod * 12500.U - 1.U) {
    clockCnt := clockCnt + 1.U
  }.otherwise {
    clockCnt := 0.U
    toSync := true.B
  }

  when(toSync) {
    syncReg := Mux(io.bidirectional, led2LightUp, (1.B << currentLed).asUInt)
    toSync := false.B
    currentLed := currentLed + 1.U
    switch(led2LightUp) { // Switch the LEDs with an FSM
      is("b10000001".U) {
        led2LightUp := "b01000010".U
        direction := 1.B
      }
      is("b01000010".U) {
        led2LightUp := Mux(direction, "b00100100".U, "b10000001".U)
      }
      is("b00100100".U) {
        led2LightUp := Mux(direction, "b00011000".U, "b01000010".U)
      }
      is("b00011000".U) {
        led2LightUp := "b00100100".U
        direction := 0.B
      }
    }
  }.otherwise {
    syncReg := 0.U
  }

  io.out := Cat(for (led <- fadeOutMods) yield led.io.out)
}
