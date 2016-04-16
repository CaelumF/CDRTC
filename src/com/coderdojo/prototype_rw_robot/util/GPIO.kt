package com.coderdojo.prototype_rw_robot.util

import com.gmail.caelum119.utils.files.FileIO
import sun.plugin.dom.exception.InvalidStateException

/**
 * First created 3/30/2016 in CoderdojoRobot
 */
object GPIO {
  data class GPIOHeader(var mode: Mode, var value: Int)
  public enum class Mode {IN, OUT}

  private val headers = Array(8, {GPIOHeader(Mode.IN, 0)})

  init {
    for(i in 408..415)

      FileIO.writeText("/sys/class/gpio/export", i)
  }

  /**
   * Sets GPIO header #[header] to [value].
   *
   * @param header Int between 1-8 designating the GPIO header you would like to set.
   * @param value Int either 0 or 1, the value you want the designated GPIO header to have.
   *
   * @throws IllegalArgumentException when the selected header is outside of 1-8.
   * @throws IllegalArgumentException when value is not 1 or 0.
   * @throws InvalidStateException when the selected header is in IN mode.
   */
  fun setValue(header: Int, value: Int) {
    if(header < 1 || header > 8 )
      throw IllegalArgumentException("header parameter is $header, outside of the legal range of 1-8")
    if(value != 0 || value != 1)
      throw IllegalArgumentException("value parameter is $value, only 1 or 0 is accepted.}")
    if(headers[header].mode == Mode.IN)
      throw InvalidStateException("Header $header is on IN mode. Setting it's value will do nothing.")

    headers[header].value = value
    FileIO.writeText("/sys/class/gpio${407 + header}/value", value)
  }

  /**
   *
   */
  fun getOutputtedValue(header: Int): Int{
    if (header < 1 || header > 8 )
      throw IllegalArgumentException("header parameter is $header, outside of the legal range of 1-8")
    if (headers[header].mode == Mode.IN)
      throw InvalidStateException("Header $header is on IN mode, so you are not setting it's output. See readValue()")
    return headers[header].value
  }

  fun readValue(header: Int): Int {
    if (header < 1 || header > 8 )
      throw IllegalArgumentException("header parameter is $header, outside of the legal range of 1-8")
    if (headers[header].mode == Mode.OUT)
      throw InvalidStateException("Header $header is on OUT mode, you are defining it's value. See getOutputtedValue()")
    return Integer.parseInt(FileIO.readText("/sys/class/gpio${407 + header}/value"))
  }
}