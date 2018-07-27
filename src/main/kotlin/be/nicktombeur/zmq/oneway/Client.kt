package be.nicktombeur.zmq.oneway

import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*

fun main(args: Array<String>) {
    ZContext().use { context ->
        val socket = context.createSocket(ZMQ.SUB)
        socket.connect("tcp://*:5556")

        val filter = if (args.isNotEmpty()) args[0] else "10001"
        socket.subscribe(filter)

        while (!Thread.currentThread().isInterrupted) {
            val reply = socket.recvStr().trim()
            val stringTokenizer = StringTokenizer(reply, " ")
            val zipCode = Integer.valueOf(stringTokenizer.nextToken())
            val temperature = Integer.valueOf(stringTokenizer.nextToken())
            val relHumidity = Integer.valueOf(stringTokenizer.nextToken())

            println("Reply: \n\tZip code: $zipCode\n\tTemperature: $temperature\n\tRel humidity: $relHumidity")
        }
    }
}