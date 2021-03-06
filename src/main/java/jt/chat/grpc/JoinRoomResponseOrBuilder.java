// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: chat.proto

package jt.chat.grpc;

public interface JoinRoomResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:chat.JoinRoomResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * whether or not this room requires a password to enter
   * </pre>
   *
   * <code>bool is_password_required = 1;</code>
   */
  boolean getIsPasswordRequired();

  /**
   * <pre>
   * list of all users in this room
   * only available if there are no errors when joining
   * </pre>
   *
   * <code>repeated .chat.User user = 2;</code>
   */
  java.util.List<jt.chat.grpc.User> 
      getUserList();
  /**
   * <pre>
   * list of all users in this room
   * only available if there are no errors when joining
   * </pre>
   *
   * <code>repeated .chat.User user = 2;</code>
   */
  jt.chat.grpc.User getUser(int index);
  /**
   * <pre>
   * list of all users in this room
   * only available if there are no errors when joining
   * </pre>
   *
   * <code>repeated .chat.User user = 2;</code>
   */
  int getUserCount();
  /**
   * <pre>
   * list of all users in this room
   * only available if there are no errors when joining
   * </pre>
   *
   * <code>repeated .chat.User user = 2;</code>
   */
  java.util.List<? extends jt.chat.grpc.UserOrBuilder> 
      getUserOrBuilderList();
  /**
   * <pre>
   * list of all users in this room
   * only available if there are no errors when joining
   * </pre>
   *
   * <code>repeated .chat.User user = 2;</code>
   */
  jt.chat.grpc.UserOrBuilder getUserOrBuilder(
      int index);
}
