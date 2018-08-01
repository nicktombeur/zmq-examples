package be.nicktombeur.zmq.multidealer

import org.zeromq.ZContext
import org.zeromq.ZMQ
import kotlin.concurrent.thread

class Server(private val address: String) {

    private val context = ZContext()
    private val workerRouters = mutableListOf<Router>()

    fun start() {
        thread(priority = 1) {
            ZContext.shadow(context).use { shadow ->
                val clients = shadow.createSocket(ZMQ.ROUTER)
                clients.bind(address)

                val workers = shadow.createSocket(ZMQ.DEALER)
                workers.bind("inproc://workers")

                for (i in 1..5) {
                    val router = Router(shadow, this)
                    workerRouters.add(router)
                    router.start()
                }

                ZMQ.proxy(clients, workers, null)
            }
        }
    }

    fun send(otherAddr: String, processName: String, msg: String) {
        thread(priority = 2) {
            ZContext.shadow(context).use { shadow ->
                val dealer = shadow.createSocket(ZMQ.DEALER)
                dealer.connect(otherAddr)
                dealer.identity = "$address $processName".toByteArray()

                Thread.sleep(1000)
                println("Sending: $msg")
                val result = dealer.send(msg)
                println("Send result: $result")
            }
        }
    }
}