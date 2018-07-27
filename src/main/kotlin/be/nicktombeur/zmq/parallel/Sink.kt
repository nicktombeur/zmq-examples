package be.nicktombeur.zmq.parallel

import org.zeromq.ZContext
import org.zeromq.ZMQ
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    ZContext().use { context ->
        val receiver = context.createSocket(ZMQ.PULL)
        receiver.bind("tcp://*:5558")

        // Wait for start of batch
        receiver.recvStr()

        // Start our clock now
        val time = measureTimeMillis {
            // Process 100 confirmations
            for (i in 1..100) {
                receiver.recvStr()

                println(if (i % 10 == 0) ":" else ".")
            }
        }

        println("Total eclipsed time: $time")

    }
}