package be.nicktombeur.zmq.multidealer

fun main(args: Array<String>) {
    val peers = mapOf(Pair("CLIENTA", "tcp://*:5555"), Pair("CLIENTB", "tcp://*:5556"))
    val server = Server("CLIENTC", "tcp://*:5557", peers)
    server.start()
    server.send("CLIENTA", "Hello ClientA!")
    server.send("CLIENTB", "Hello ClientB!")
}