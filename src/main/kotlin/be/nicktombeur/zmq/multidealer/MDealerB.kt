package be.nicktombeur.zmq.multidealer

fun main(args: Array<String>) {
    val peers = mapOf(Pair("CLIENTA", "tcp://*:5555"), Pair("CLIENTC", "tcp://*:5557"))
    val server = Server("CLIENTB", "tcp://*:5556", peers)
    server.start()
    server.send("CLIENTA", "Hello ClientA!")
}
