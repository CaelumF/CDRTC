package com.coderdojo.network_tags.behaviours

/**
 * First created 5/25/2016 in CoderdojoRobot
 */
public class DebugTag(val run: () -> Unit){
    init{
        run.invoke()
    }
}