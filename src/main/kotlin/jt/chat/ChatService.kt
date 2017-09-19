package jt.chat

import io.grpc.stub.StreamObserver
import jt.chat.grpc.ChatGrpc
import jt.chat.grpc.JoinRoomRequest
import jt.chat.grpc.JoinRoomResponse
import jt.chat.grpc.User

class ChatService : ChatGrpc.ChatImplBase() {

    override fun joinRoom(request: JoinRoomRequest, responseObserver: StreamObserver<JoinRoomResponse>) {
        val incomingUsername = request.user.username
        println("$incomingUsername has joined the room")
        val response = JoinRoomResponse.newBuilder()
                .addUser(User.newBuilder().setUsername(incomingUsername))
                .setIsPasswordRequired(false)
                .build()
        responseObserver.apply {
            onNext(response)
            onCompleted()
        }
    }
}