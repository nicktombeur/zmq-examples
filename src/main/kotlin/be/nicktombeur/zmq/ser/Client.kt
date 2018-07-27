package be.nicktombeur.zmq.ser

import com.fasterxml.jackson.databind.ObjectMapper
import org.msgpack.core.MessagePack
import org.msgpack.jackson.dataformat.MessagePackFactory
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        val socket = context.createSocket(ZMQ.REQ)
        socket.connect("tcp://*:5555")

        val person = Person("John", "Doe", 100001)
        val objectMapper = ObjectMapper(MessagePackFactory())
        val bytes = objectMapper.writeValueAsBytes(person)

        while (!Thread.currentThread().isInterrupted) {
            println("Send: $person")
            socket.send(bytes)

            val reply = String(socket.recv(), ZMQ.CHARSET)
            println("Received: $reply")
        }
    }
}

data class Person(val name: String = "", val lastName: String = "", val zipCode: Int = 0)