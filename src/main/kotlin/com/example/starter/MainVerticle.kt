package com.example.starter

import com.example.starter.ola.OlaReply
import com.example.starter.ola.OlaRequest
import com.example.starter.ola.OlaProto
import com.example.starter.ola.OlaGrpcKt
import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions
import io.vertx.grpc.server.GrpcServer
import io.vertx.grpc.server.GrpcServerRequest
import io.vertx.grpc.server.GrpcServerResponse


class MainVerticle : AbstractVerticle() {
  override fun start() {
    // Create the server
    val rpcServer = GrpcServer.server(vertx)

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure { cause: Throwable -> cause.printStackTrace() }
        .onSuccess { println("-- Server is running on port 8080 ---") }
  }

    internal class OlaService: OlaGrpcKt.OlaCoroutineImplBase() {

          suspend fun SayOla(request: OlaRequest): OlaReply.Builder? {

              println("Sending Reply to Request ${request.name}...")

              var reply = OlaReply.newBuilder()
                  reply.setMessage("hello ${request.name}")

              return reply
          }


    }


/*
    fun requestResponse(server: GrpcServer) {
        server.callHandler(OlaGrpc.getSayHelloMethod()) { request: GrpcServerRequest<OlaRequest?, OlaReply?> ->
            request.handler { hello: OlaRequest? ->
                val response: GrpcServerResponse<OlaRequest?, OlaReply?> = request.response()
                val reply: OlaReply = OlaReply.newBuilder().setMessage("Hello " + hello?.name).build()
                response.end(reply)
            }
        }
    }

*/
}


/*
    // The rpc service
    rpcServer.callHandler(OlaServiceGrpc.getSayOlaMethod(), request -> {
        request
          .last()
          .onSuccess( msg -> {
            System.out.println("Olá " + msg.getName());
            request.response().end(OlaReply.newBuilder().setMessage(msg.getName()).build());
          });
      });

 */

fun main() {
  Vertx.vertx().deployVerticle(MainVerticle())
}
