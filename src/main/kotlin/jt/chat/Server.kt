package jt.chat

import io.grpc.ServerBuilder

/**
 * Server that manages startup/hosting of a room
 */
object Server {

    private var server: io.grpc.Server? = null

    fun start(port: Int, blocking: Boolean = false) {
        server = ServerBuilder.forPort(port)
                .addService(ChatService())
                .build()
                .start()
        println("Server started, listening on port $port")

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down")
                Server::stop
                System.err.println("*** server shut down")
            }
        })

        if (blocking) blockUntilShutdown()
    }

    fun stop() {
        server?.shutdown()
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private fun blockUntilShutdown() {
        server?.awaitTermination()
    }
}

