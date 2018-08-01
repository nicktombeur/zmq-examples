package be.nicktombeur.zmq.multi

import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    Server().use { it.start() }
}

class Server : ZContext() {

    fun start() {
        val clients = createSocket(ZMQ.ROUTER)
        clients.bind("tcp://*:5555")

        val workers = createSocket(ZMQ.DEALER)
        workers.bind("inproc://workers")

        for (i in 0..5) {
            Worker(this).start()
        }

        ZMQ.proxy(clients, workers, null)
    }
}

class Worker(val context: ZContext) : Thread() {

    override fun run() {
        val socket = context.createSocket(ZMQ.REP)
        socket.connect("inproc://workers")

        while (!Thread.currentThread().isInterrupted) {
            val request = socket.recvStr()
            println("${Thread.currentThread().name} Received request: [$request]")

            Thread.sleep(1000) // do some work

            socket.send("world")
        }
    }
}