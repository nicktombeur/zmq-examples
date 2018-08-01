package be.nicktombeur.zmq.multidealer

fun main(args: Array<String>) {
    val server = Server("tcp://*:5556")
    server.start()
    server.send("tcp://*:5555","hello", "Hello ClientA!")
}
