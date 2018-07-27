package be.nicktombeur.zmq.reqrep

import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val socket = context.createSocket(ZMQ.REQ)
        socket.connect("tcp://*:5555")

        while (!Thread.currentThread().isInterrupted) {
            println("Send: Hello")
            socket.send("Hello")

            val reply = String(socket.recv(), ZMQ.CHARSET)
            println("Received: $reply")
        }
    }
}