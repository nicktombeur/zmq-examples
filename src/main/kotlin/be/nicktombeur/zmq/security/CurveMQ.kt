package be.nicktombeur.zmq.security

import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    val serverKeys = ZMQ.Curve.generateKeyPair()
    val clientKeys = ZMQ.Curve.generateKeyPair()

    ZContext().use { context ->
        val server = context.createSocket(ZMQ.PUSH)
        server.curveServer = true
        server.curvePublicKey = serverKeys.publicKey.toByteArray()
        server.curveSecretKey = serverKeys.secretKey.toByteArray()
        server.bind("tcp://*:7210")

        val client = context.createSocket(ZMQ.PULL)
        client.curvePublicKey = clientKeys.publicKey.toByteArray()
        client.curveSecretKey = clientKeys.secretKey.toByteArray()
        client.curveServerKey = serverKeys.publicKey.toByteArray()

        client.connect("tcp://*:7210")

        server.send("Hello, World!")
        println(client.recvStr())
    }
}