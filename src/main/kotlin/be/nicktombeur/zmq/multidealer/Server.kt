package be.nicktombeur.zmq.multidealer

import org.zeromq.ZContext
import org.zeromq.ZMQ
import kotlin.concurrent.thread

class Server(private val id: String, private val address: String, private val peers: Map<String, String>) {

    private val context = ZContext()
    private val workerRouters = mutableListOf<Router>()

    fun start() {
        val thread = thread(priority = 1, start = false) {
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
                workers.close()
                clients.close()
            }
        }

        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            println("Interrupt received, killing server...")

            context.close()
            for (router in workerRouters) {
                router.interrupt()
                router.join()
            }
        })

        thread.start()
    }

    fun send(id: String, msg: String) {
        thread(priority = 2) {
            ZContext.shadow(context).use { shadow ->
                val dealer = shadow.createSocket(ZMQ.DEALER)
                dealer.connect(peers[id])
                dealer.identity = this.id.toByteArray()

                Thread.sleep(1000)
                println("Sending: $msg")
                val result = dealer.send(msg)
                println("Send result: $result")

                dealer.close()
            }
        }
    }
}