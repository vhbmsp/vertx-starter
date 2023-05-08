package com.example.starter

import com.example.starter.ola.OlaGrpcKt
import com.example.starter.ola.OlaReply
import com.example.starter.ola.OlaRequest
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.grpc.server.GrpcServer
import io.vertx.grpc.server.GrpcServiceBridge

class MainVerticle : AbstractVerticle() {
  override fun start() {


      val service: OlaGrpcKt.OlaCoroutineImplBase = object : OlaGrpcKt.OlaCoroutineImplBase() {
          override suspend fun sayOla(request: OlaRequest): OlaReply {
              val name = request.getName()
              // println("sayOla com nome: $name")
              return OlaReply.newBuilder().setMessage("Olá chouriço $name").build()
          }
      }

      // Create the server
      val rpcServer = GrpcServer.server(vertx)
      GrpcServiceBridge
          .bridge(service)
          .bind(rpcServer)

      // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure { cause: Throwable -> cause.printStackTrace() }
        .onSuccess { println("-- Server is running on port 8080 ---") }
  }
}

fun main() {
  Vertx.vertx().deployVerticle(MainVerticle())
}
