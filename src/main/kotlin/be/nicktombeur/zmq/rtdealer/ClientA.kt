package be.nicktombeur.zmq.rtdealer

import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val router = context.createSocket(ZMQ.ROUTER)
        router.bind("tcp://*:5555")

        // Connect to ClientB
        val dealer = context.createSocket(ZMQ.DEALER)
        dealer.connect("tcp://*:5556")
        dealer.identity = "CLIENTA".toByteArray()

        while (!Thread.currentThread().isInterrupted) {
            println("Sending: Hello ClientB!")

            dealer.send("Hello ClientB!")
            println("Identity: ${router.recvStr()}")
            println("Message: ${router.recvStr()}")
            Thread.sleep(1000)
        }
    }
}