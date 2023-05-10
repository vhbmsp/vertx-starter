package com.example.starter

import io.vertx.core.Vertx


fun main() {
    // Start GRPC Server
    Vertx.vertx().deployVerticle(GrpcVerticle())

    // Start Web Server
    Vertx.vertx().deployVerticle(RestVerticle())
}




