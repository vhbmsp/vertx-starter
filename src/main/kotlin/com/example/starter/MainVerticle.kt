package com.example.starter

import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.grpc.server.GrpcServer
import io.vertx.grpc.server.GrpcServerRequest

import com.example.starter.HelloRequest;
import com.example.starter.HelloReply



class MainVerticle : AbstractVerticle() {
  override fun start() {
    // Create the server
    val rpcServer = GrpcServer.server(vertx)

    // The rpc service
    rpcServer.callHandler(GreeterGrpc.getSayHelloMethod(),
      Handler<GrpcServerRequest<HelloRequest, HelloReply?>> { request: GrpcServerRequest<HelloRequest, HelloReply?> ->
        request
          .last()
          .onSuccess(Handler<HelloRequest> { msg: HelloReply ->
            System.out.println("Hello " + msg.getName())
            request.response().end(HelloReply.newBuilder().setMessage(msg.getName()).build())
          })
      })


    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure { cause: Throwable -> cause.printStackTrace() }
  }
}

fun main() {
  val options = VertxOptions()
  Vertx.vertx().deployVerticle(MainVerticle())
}
