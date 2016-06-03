package com.coderdojo.network_tags.behaviours

import com.coderdojo.prototype_rw_robot.services.MovementControl

/**
 * First created 4/28/2016 in CoderdojoRobotController
 */
public class MoveForward(val LWspeed: Float = 1.0f, val RWSpeed: Float, val durationMillis: Int = 10) : Behaviour(){
    override val behaviourIdentifier: String = "MoveForward"

    companion object {
        @JvmStatic val serialVersionUID: Long = 590473204662185747
    }

    init{

    }

    override fun onReceive(){
        MovementControl.moveForward(LWspeed + RWSpeed, durationMillis)
        println("MoveForward embedded behaviour constructed")
    }
}