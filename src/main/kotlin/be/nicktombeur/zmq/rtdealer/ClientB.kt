package be.nicktombeur.zmq.rtdealer

import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val router = context.createSocket(ZMQ.ROUTER)
        router.bind("tcp://*:5556")

        // Connect to ClientA
        val dealer = context.createSocket(ZMQ.DEALER)
        dealer.connect("tcp://*:5555")
        dealer.identity = "CLIENTB".toByteArray()

        while (!Thread.currentThread().isInterrupted) {
            println("Identity: ${router.recvStr()}")
            println("Message: ${router.recvStr()}")
            println("Sending: Hello ClientA!")
            dealer.send("Hello ClientA!")
        }
    }
}