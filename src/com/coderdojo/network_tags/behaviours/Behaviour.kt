package com.coderdojo.network_tags.behaviours

import com.gmail.caelum119.utils.network.NetworkTag

/**
 * First created 6/3/2016 in CoderdojoRobot
 */
abstract class Behaviour: NetworkTag() {
    /**
     * Called when the event is received
     */
    abstract fun onReceive()

    abstract val behaviourIdentifier: String

    companion object {
        @JvmStatic val serialVersionUID: Long = 590473204662285747
    }

}