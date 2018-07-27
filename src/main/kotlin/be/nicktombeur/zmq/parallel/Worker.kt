package be.nicktombeur.zmq.parallel

import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val receiver = context.createSocket(ZMQ.PULL)
        receiver.connect("tcp://*:5557")

        val sender = context.createSocket(ZMQ.PUSH)
        sender.connect("tcp://*:5558")

        while (!Thread.currentThread().isInterrupted) {
            println(receiver.recvStr())
            Thread.sleep(1000) // do the work
            sender.send("") // send results to sink
        }
    }
}