package com.coderdojo.network_tags.behaviours

import com.coderdojo.prototype_rw_robot.services.MovementControl

/**
 * First created 6/3/2016 in CoderdojoRobot
 */
class Turn(val speed: Float, val direction: Float, val duration: Int): Behaviour(){
    override val behaviourIdentifier: String = "Turn"

    companion object {
        @JvmStatic val serialVersionUID: Long = 590473212662185747
    }

    override fun onReceive() {
        MovementControl.turn(speed, direction, duration)
        println("Turn behaviour activated")
    }
}