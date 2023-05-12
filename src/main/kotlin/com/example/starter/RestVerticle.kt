package com.example.starter

import com.example.starter.ola.OlaGrpcKt
import com.example.starter.ola.OlaReply
import com.example.starter.ola.OlaRequest
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.redis.client.Redis
import io.vertx.redis.client.RedisAPI
import io.vertx.redis.client.RedisConnection
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RestVerticle : AbstractVerticle() {
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
                println(" === Redis connected @localhost:6379 === ")
                redisConnection = it
                println(redisConnection.toString())
            }


        // Create the router
        // Create the router
        val router: Router = Router.router(vertx);


        router["/"].handler { rc: RoutingContext ->
            rc.response().end("Hello")
        }

        router["/sayola/:name"].handler { rc: RoutingContext ->

            val name = rc.pathParam("name")
            val redis: RedisAPI = RedisAPI.api(redisConnection)
            var messageItem: Message = Message()


            // get message from cache
            val response = redis.jsonGet(listOf("myjsonhash")).onComplete {
                // println("Redis result: ${it.result()}")
                messageItem = Message.fromJson(it.result().toString())
                messageItem.message+= name
                rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                rc.response().end(Message.toJson(messageItem))
            }

        }


        // start the Web server
        vertx.createHttpServer().requestHandler(router).listen(80)
            .onFailure { cause: Throwable -> cause.printStackTrace() }
            .onSuccess {
                println("-- Web Server is running on port 80 ---")
            }

    }
}

