package be.nicktombeur.zmq.multidealer

import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*

class Router(private val context: ZContext, private val server: Server) : Thread() {
    override fun run() {
        val router = context.createSocket(ZMQ.ROUTER)
        router.connect("inproc://workers")

        println("${Thread.currentThread().name} is listening")
        while (!Thread.currentThread().isInterrupted) {
            val workerId = router.recvStr()
            val peer = router.recvStr()
            val msg = router.recvStr()
            println("${Thread.currentThread().name} Received request:\n\tWorker ID: $workerId\n\tPeer: $peer\n\tMessage: $msg")

            val tokenizer = StringTokenizer(peer, " ")
            val peerAddress = tokenizer.nextToken()
            val process = tokenizer.nextToken()

            if ("hello" == process) {
                server.send(peerAddress, "returnHello", "Greetings back!")
            }

        }
    }
}