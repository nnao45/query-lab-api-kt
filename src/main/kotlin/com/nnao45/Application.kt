package com.nnao45

import com.nnao45.plugins.configureDDL
import com.nnao45.plugins.configureRouting
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0") {
        install(CallLogging)
        configureRouting()
        configureDDL()
    }.start(wait = true)
}
