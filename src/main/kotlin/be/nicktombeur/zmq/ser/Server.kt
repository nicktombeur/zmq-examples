package be.nicktombeur.zmq.ser

import com.fasterxml.jackson.databind.ObjectMapper
import org.msgpack.jackson.dataformat.MessagePackFactory
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val socket = context.createSocket(ZMQ.REP)
        socket.bind("tcp://*:5555")

        val objectMapper = ObjectMapper(MessagePackFactory())

        while (!Thread.currentThread().isInterrupted) {
            val person = objectMapper.readValue(socket.recv(), Person::class.java)
            println("Received: $person")

            println("Send: World")
            socket.send("World")
            Thread.sleep(1000) // Do some work
        }
    }
}