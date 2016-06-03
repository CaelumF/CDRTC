package com.coderdojo.prototype_rw_robot

import com.coderdojo.network_tags.behaviours.NetworkedBehaviour
import com.gmail.caelum119.utils.network.server.TCPServer

/**
 * First created 4/28/2016 in CoderdojoRobot
 */
object Server {
  val tcpServer = TCPServer(hostPort = 6005)
    var out = false
  init {
    println("Starting TCP server on 6005")
    tcpServer.start()
    tcpServer.addConnectionEstablishedListener {
      it.addListener<NetworkedBehaviour>(NetworkedBehaviour::class.java,  {
          it.behaviour.onReceive()
//          if(out) {
//              GPIO.setValue(4, false)
//              println("Turning on ")
//              out = false
//          }
//          else {
//              GPIO.setValue(4, true)
//              println("Turning off")
//              out = true
//          }
      })
    }
  }
}