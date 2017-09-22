package jt.chat

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import jt.chat.grpc.ChatGrpc
import jt.chat.grpc.JoinRoomRequest
import jt.chat.grpc.JoinRoomResponse
import jt.chat.grpc.User
import java.util.concurrent.TimeUnit

/**
 * This class manages and controls the behavior of a client connected to a server
 */
class Client(private val application: Application, private var host: String, private var port: Int) {

    /* Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
    needing certificates. */
    private var channel: ManagedChannel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext(true)
            .build()
    private var blockingStub: ChatGrpc.ChatBlockingStub
    private var asyncStub: ChatGrpc.ChatStub

    private var shouldEndCmdLoop: Boolean = false

    /**
     * Client state machine
     */
    enum class State {
        REQUEST_USERNAME,
        JOIN_ROOM,
        CONNECTED,
        LEAVE_ROOM,
        DISCONNECTED
    }

    /**
     * The current state of the client
     */
    private var state: State

    /**
     * The current username
     */
    private var username: String?

    init {
        blockingStub = ChatGrpc.newBlockingStub(channel)
        asyncStub = ChatGrpc.newStub(channel)
        state = State.REQUEST_USERNAME
        username = null
    }

    //region client functions

    fun shutdown(delay: Long = 0L) {
        channel.shutdown().awaitTermination(delay, TimeUnit.SECONDS)
    }

    /**
     * Begins the client loop through the states
     */
    fun start() {
        while (!shouldEndCmdLoop) {
            when (state) {
                State.REQUEST_USERNAME -> {
                    requestUsername {
                        state = State.JOIN_ROOM
                    }
                }
                State.JOIN_ROOM -> {
                    joinRoom ({
                        state = State.CONNECTED
                    }, this::exitToMainCmdLoop)
                }
                State.CONNECTED -> TODO()
                State.LEAVE_ROOM -> TODO()
                State.DISCONNECTED -> TODO()
            }
        }
    }

    /**
     * Terminates client loop and resumes the main application loop
     */
    private fun exitToMainCmdLoop() {
        shouldEndCmdLoop = true
        application.runMainCmdLoop()
    }

    /**
     * Asks user for a username to join a room
     */
    private fun requestUsername(onComplete: () -> Unit) {
        print("Join room as ")
        readInput().let {
            username = it
            onComplete()
        }
    }

    /**
     * Joins a room using the specified username
     */
    private fun joinRoom(onComplete: () -> Unit, onFailure: () -> Unit) {
        println("Attempting to join room at $host on port $port...")

        try {
            val request = JoinRoomRequest.newBuilder().setUser(User.newBuilder().setUsername(username).build()).build()
            val response = blockingStub.joinRoom(request)
            println("Response from server is $response")
            onComplete()
        }
        catch (ex: StatusRuntimeException) {
            System.err.println("Connection to server failed with status ${ex.status}")
            onFailure()
        }
    }

    //endregion
}