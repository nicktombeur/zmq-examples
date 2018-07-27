package be.nicktombeur.zmq.parallel

import be.nicktombeur.zmq.oneway.random
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val sender = context.createSocket(ZMQ.PUSH)
        sender.bind("tcp://*:5557")

        val sink = context.createSocket(ZMQ.PUSH)
        sink.connect("tcp://*:5558")

        println("Press Enter when the workers are ready: ")
        readLine()
        println("Send tasks to workers...\n")

        // The first message is "0" and signals start of batch
        sink.send("0")

        var totalMsec = 0
        for (i in 1..100) {
            val workload = (1..100).random() + 1
            totalMsec += workload
            sender.send("$workload")
        }
        println("Total expected cost: $totalMsec msec\n")
    }
}