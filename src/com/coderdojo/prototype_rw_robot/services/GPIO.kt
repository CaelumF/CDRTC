package com.coderdojo.prototype_rw_robot.services

import com.coderdojo.exceptions.InvalidStateException
import com.gmail.caelum119.utils.files.FileIO
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths

/**
 * First created 3/30/2016 in CoderdojoRobot
 * Controls GPIO headers, keeps track of their value etc.
 *
 * Use [MovementControl] instead for higher level applications
 */


object GPIO {

    data class GPIOHeader(var direction: GPIO.Direction, var value: Int)

    public enum class Direction {IN, OUT}

    private val headers = Array(8, {GPIOHeader(Direction.OUT, 0)})

    val gpioWatcher = FileSystems.getDefault().newWatchService()

    init {
        //Check if the headers are activated and activate the headers by writing their gpio suffix to /export if they're not
        for(gpioSuffix in 408 .. 415) {
            if (!Files.isDirectory(Paths.get("/sys/class/gpio/gpio$gpioSuffix"))) {
                FileIO.writeText("/sys/class/gpio/export", gpioSuffix)
            }
        }
//        gpioWatcher.
        //Set all headers to out
        for(header in 0 .. 7){
            if(header < 5)
                setDirection(header, Direction.OUT)
            else
                setDirection(header, Direction.IN)
        }
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
        if(header < 0 || header > 7 )
          throw IllegalArgumentException("header parameter is $header, outside of the legal range of 0-7")
        if(!value.equals(0) && !value.equals(1))
          throw IllegalArgumentException("value parameter is $value, only 1 or 0 is accepted.}")
        if(headers[header].direction == GPIO.Direction.IN)
          throw InvalidStateException("Header $header is on IN mode. Setting it's value will do nothing.")

        FileIO.writeText("/sys/class/gpio/gpio${408 + header}/value", value)
        headers[header].value = value
    }

    fun setValue(header: Int, value: Boolean){
        setValue(header, if(value) 1 else 0)
    }

    /**
    *
    */
    fun getOutputtedValue(header: Int): Int{
        if (header < 0 || header > 7 )
          throw IllegalArgumentException("header parameter is $header, outside of the legal range of 0-7")
        if (headers[header].direction == Direction.IN)
          throw InvalidStateException("Header $header is on IN mode, so you are not setting it's output. See readValue()")

        return headers[header].value
    }

    fun readValue(header: Int): Int {
        if (header < 0 || header > 7 )
          throw IllegalArgumentException("header parameter is $header, outside of the legal range of 0-7")
        if (headers[header].direction == Direction.OUT)
          throw InvalidStateException("Header $header is on OUT mode, setting it would do nothing. See getOutputtedValue() or setDirection()")

        return Integer.parseInt(FileIO.readText("/sys/class/gpio/gpio${408 + header}/value"))
    }

    /**
     * Sets the direction of [header]
     */
    fun setDirection(header: Int, direction: Direction) {
        if (header < 0 || header > 7 )
            throw IllegalArgumentException("header parameter is $header, outside of the legal range of 0-7")

        val directionName: String = if(direction == Direction.OUT) "out" else "in"
        FileIO.writeText("/sys/class/gpio/gpio${408 + header}/direction", directionName)
        headers[header].direction = direction
    }

    /**
     * Set ths direction of all [headers] to [direction]
     */
    fun setDirection(direction: Direction, vararg headers: Int){
        for(header in headers){
            setDirection(header, direction)
        }
    }
}