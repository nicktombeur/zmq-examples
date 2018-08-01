package be.nicktombeur.zmq.multidealer

fun main(args: Array<String>) {
    val peers = mapOf(Pair("CLIENTB", "tcp://*:5556"), Pair("CLIENTC", "tcp://*:5557"))
    val server = Server("CLIENTA", "tcp://*:5555", peers)
    server.start()

    do {
        print("Enter a message: ")
        val input = readLine()

        if (!input.equals("exit")) server.send("CLIENTB", input.orEmpty())
    } while (!input.equals("exit"))
}
