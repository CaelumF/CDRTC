package com.coderdojo.prototype_rw_robot.services

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * First created 6/2/2016 in CoderdojoRobot
 *
 * Will eventually be used to prevent different control sources from conflicting by addnig a layer of abstraction,
 * and probably throwing an exception when commands conflict(eg. Moving forward while you're still driving backward)
 */


object MovementControl {

    enum class WheelDirection {FORWARD, BACKWARD, STATIONARY}

    val gpioUpdateSpeedMillis: Long = 10

    /////////////////////////////////////////////
    //Pin headers
    private val motorControlStandbyPin = 7 //415
    private val lwPWMPin = 5               //413
    private val rwPWMPin = 4               //412

    private val rwDirectionPin1 = 3        //411
    private val rwDirectionPin2 = 1        //409

    private val lwDirectionPin1 = 2        //410
    private val lwDirectionPin2 = 0        //408
    //-------------------------------------------
    ////////////////////////////////////////////
    //GPIO output values
    private var lwDirection: WheelDirection = WheelDirection.FORWARD
    private var rwDirection: WheelDirection = WheelDirection.FORWARD

    private var motorsEnabled = true
    //------------------------------------------

    var currentActionDurationTicks: Int = 0
    init{
        println("Starting MovementControl service")
        //Set all pins to OUT
        GPIO.setDirection(GPIO.Direction.OUT, 7, 6, 5, 4, 3, 2, 1, 0)

        //Periodically update GPIOs to reflect movement variables
        ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(Runnable {

            GPIO.setValue(motorControlStandbyPin, motorsEnabled) // Activate motor controller
            //TODO: PWM integration for LW
            GPIO.setValue(lwPWMPin, true)
            //TODO: PWM integration for RW
            GPIO.setValue(rwPWMPin, true)
            when (rwDirection) {
            //Set both direction values so that RW goes forward
                WheelDirection.FORWARD -> {

                    GPIO.setValue(rwDirectionPin1, true)
                    GPIO.setValue(rwDirectionPin2, false)
                }
            //Set both direction values so that RW goes backward
                WheelDirection.BACKWARD -> {

                    GPIO.setValue(rwDirectionPin1, false)
                    GPIO.setValue(rwDirectionPin2, true)
                }
            //Set both direction values so that RW is stationary
                WheelDirection.STATIONARY -> {

                    GPIO.setValue(rwDirectionPin1, false)
                    GPIO.setValue(rwDirectionPin2, false)
                }
            }
            when (lwDirection) {
            //Set both direction values so that RW goes forward
                WheelDirection.FORWARD -> {
                    println("forward")
                    GPIO.setValue(lwDirectionPin1, true)
                    GPIO.setValue(lwDirectionPin2, false)
                }
            //Set both direction values so that RW goes backward
                WheelDirection.BACKWARD -> {
                    GPIO.setValue(lwDirectionPin1, false)
                    GPIO.setValue(lwDirectionPin2, true)
                }
            //Set both direction values so that RW is stationary
                WheelDirection.STATIONARY -> {
                    GPIO.setValue(lwDirectionPin1, false)
                    GPIO.setValue(lwDirectionPin2, false)
                }
            }

            //Reset GPIO values to stationary after duration runs out so that the motors aren't always on
            if(currentActionDurationTicks < 0) {
                lwDirection = WheelDirection.STATIONARY
                rwDirection = WheelDirection.STATIONARY
                motorsEnabled = false
            }else{
                currentActionDurationTicks--
            }
        }, gpioUpdateSpeedMillis, gpioUpdateSpeedMillis, TimeUnit.MILLISECONDS)

    }

    /**
     * TODO: Duration
     * Sets the controller to activate headers [lwDirectionPin1] and [lwDirectionPin2] for [duration] ticks. A tick is defined by [gpioUpdateSpeedMillis]
     */
    fun moveForward(speed: Float, duration: Int){
        currentActionDurationTicks = duration
        lwDirection = WheelDirection.FORWARD
        rwDirection = WheelDirection.FORWARD
        motorsEnabled = true

        println("Told to move forward")
    }

    /**
     * Activates motors to turn and/or move.
     * [direction]: Direction and effort to turn with. Positive numbers between 0 and 1 go from forward to on-the-spot right turn.
     * Negative numbers go from 0 to -1 go from forward to on-the-sport left turn
     * [speed]: The speed modifier for the motors to turn with, where 0 is none and 1 is full speed, 0.5 half speed etc.
     */
    fun turn(speed: Float, direction: Float, duration: Int){
        println("Turning $speed $direction $direction")
        //TODO: Incorporate PWM for full speed
        currentActionDurationTicks = duration
        motorsEnabled = true
        when {
            direction == 0.toFloat() -> {
                rwDirection = WheelDirection.FORWARD
                lwDirection = WheelDirection.FORWARD
            }
            //NSFW(Not safe for web-developers)
            direction < 0 -> {
                rwDirection = WheelDirection.FORWARD
                lwDirection = WheelDirection.BACKWARD
            }
            direction > 0 -> {
                rwDirection = WheelDirection.BACKWARD
                lwDirection = WheelDirection.FORWARD
            }
        }


    }
}