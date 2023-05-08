package com.example.starter

import com.example.starter.ola.OlaGrpcKt
import com.example.starter.ola.OlaReply
import com.example.starter.ola.OlaRequest
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.grpc.server.GrpcServer
import io.vertx.grpc.server.GrpcServiceBridge
import io.vertx.kotlin.coroutines.await
import io.vertx.redis.client.Redis
import io.vertx.redis.client.RedisAPI
import io.vertx.redis.client.RedisConnection


class MainVerticle : AbstractVerticle() {
  override fun start() {

      var redisConnection: RedisConnection? = null

      Redis.createClient(
          vertx,  // The client handles REDIS URLs. The select database as per spec is the
          // numerical path of the URL and the password is the password field of
          // the URL authority
          "redis://:enrichment_db@localhost:6379"
      )
          .connect()
          .onSuccess {
              print(" === Redis connected @localhost:6379 === ")
              redisConnection = it
              println(redisConnection.toString())
          }

      val service: OlaGrpcKt.OlaCoroutineImplBase = object : OlaGrpcKt.OlaCoroutineImplBase() {
          override suspend fun sayOla(request: OlaRequest): OlaReply {
              val name = request.getName()
              var message: String? =  ""
              // println("sayOla com nome: $name")

              val redis: RedisAPI = RedisAPI.api(redisConnection)

              var response = redis.get("myhash").onComplete { value -> message = value.result().toString()}

              return OlaReply.newBuilder().setMessage("$message $name").build()
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
        .onSuccess {
            println("-- Server is running on port 8080 ---")
        }
  }
}

fun main() {
  Vertx.vertx().deployVerticle(MainVerticle())
}
