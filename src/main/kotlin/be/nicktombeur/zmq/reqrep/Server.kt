package be.nicktombeur.zmq.reqrep

import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val socket = context.createSocket(ZMQ.REP)
        socket.bind("tcp://*:5555")

        while (!Thread.currentThread().isInterrupted) {
            val reply = String(socket.recv(), ZMQ.CHARSET)
            println("Received: $reply")

            println("Send: World")
            socket.send("World")
            Thread.sleep(1000) // Do some work
        }
    }
}