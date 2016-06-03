package com.coderdojo.prototype_rw_robot

import com.coderdojo.prototype_rw_robot.services.GPIO

/**
 * First created 3/30/2016 in CoderdojoRobot
 */
fun main(args: Array<String>) {
    Server
    for(i in 0 .. 100) {
        println("On")
        GPIO.setValue(3, 1)
        GPIO.setValue(4, 1)
        Thread.sleep(100)
        println("Off")
        GPIO.setValue(4, 0)
        GPIO.setValue(3, 0)
        Thread.sleep(100)
    }

}
