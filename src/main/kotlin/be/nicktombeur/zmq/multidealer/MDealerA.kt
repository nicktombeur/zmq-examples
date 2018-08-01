package be.nicktombeur.zmq.multidealer

fun main(args: Array<String>) {
    val server = Server("tcp://*:5555")
    server.start()

    do {
        print("Enter a message: ")
        val input = readLine()

        if (!input.equals("exit")) server.send("tcp://*:5556", "custom", input.orEmpty())
    } while (!input.equals("exit"))
}
