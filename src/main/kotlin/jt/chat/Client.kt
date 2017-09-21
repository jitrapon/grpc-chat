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
 * Client that connects to the server
 */
class Client(private var host: String, private var port: Int) {

    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
    // needing certificates.
    private var channel: ManagedChannel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext(true)
            .build()
    private var blockingStub: ChatGrpc.ChatBlockingStub
    private var asyncStub: ChatGrpc.ChatStub

    /**
     * Client state machine states
     */
    enum class State {
        REQUEST_USERNAME,
        JOIN_ROOM,
        JOINED_ROOM,
        LEAVE_ROOM,
        LEFT_ROOM
    }

    private var state: State
    private var username: String?

    init {
        blockingStub = ChatGrpc.newBlockingStub(channel)
        asyncStub = ChatGrpc.newStub(channel)
        state = State.REQUEST_USERNAME
        username = null
    }

    fun shutdown(delay: Long = 0L) {
        channel.shutdown().awaitTermination(delay, TimeUnit.SECONDS)
    }

    //region client functions

    /**
     * Run the client state-machine loop
     */
    fun run() {
        loop@ while (true) {
            when (state) {
                Client.State.REQUEST_USERNAME -> {
                    requestUsername(State.JOIN_ROOM)
                }
                Client.State.JOIN_ROOM -> {
                    if (!joinRoom(State.JOINED_ROOM)) {
                        break@loop
                    }
                }
                Client.State.JOINED_ROOM -> {
                    sendMessage(State.LEAVE_ROOM)
                }
                Client.State.LEAVE_ROOM -> TODO()
                Client.State.LEFT_ROOM -> TODO()
            }
        }
    }

    private fun requestUsername(nextState: State) {
        print("Your name in the room will be: ")
        readInput().let {
            username = it
            state = nextState
        }
    }

    private fun joinRoom(nextState: State): Boolean {
        println("Attempting to join room at $host on port $port...")

        val request = JoinRoomRequest.newBuilder().setUser(User.newBuilder().setUsername(username).build()).build()
        val response: JoinRoomResponse
        try {
            response = blockingStub.joinRoom(request)
        }
        catch (ex: StatusRuntimeException) {
            System.err.println("RPC failed: ${ex.status}")
            return false
        }
        state = nextState
        return true
    }

    private fun sendMessage(nextState: State) {

    }

    //endregion
}