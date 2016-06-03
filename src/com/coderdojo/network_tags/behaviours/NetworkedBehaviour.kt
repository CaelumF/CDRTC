package com.coderdojo.network_tags.behaviours

import com.gmail.caelum119.utils.network.NetworkTag

/**
 * First created 6/3/2016 in CoderdojoRobot
 *  Just a wrapper class to contain a Behaviour, so that type-specific network events can pick up any type of Behaviour.
 */
class NetworkedBehaviour(val behaviour: Behaviour): NetworkTag(){

}