package be.nicktombeur.zmq.oneway

import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*

fun main(args: Array<String>) {
    ZContext().use { context ->
        val socket = context.createSocket(ZMQ.PUB)
        socket.bind("tcp://*:5556")

        while (!Thread.currentThread().isInterrupted) {
            val zipCode = (0..100000).random()
            val temperature = (0..215).random() - 80
            val relHumidity = (0..50).random() + 10

            println("Sending: $zipCode")
            socket.send("$zipCode $temperature $relHumidity")
            //Thread.sleep(1000) // do some work
        }
    }
}

fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) + start