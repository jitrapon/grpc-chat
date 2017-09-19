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

    init {
        blockingStub = ChatGrpc.newBlockingStub(channel)
        asyncStub = ChatGrpc.newStub(channel)
    }

    fun shutdown(delay: Long = 0L) {
        channel.shutdown().awaitTermination(delay, TimeUnit.SECONDS)
    }

    //region client functions

    fun joinRoom() {
        println("Attempting to join room at $host on port $port...")

        val request = JoinRoomRequest.newBuilder().setUser(User.newBuilder().setUsername("yoshi3003").build()).build()
        val response: JoinRoomResponse
        try {
            response = blockingStub.joinRoom(request)
        }
        catch (ex: StatusRuntimeException) {
            System.err.println("RPC failed: ${ex.status}")
            return
        }
        println("Room password required: ${response.isPasswordRequired})")
    }

    //endregion
}