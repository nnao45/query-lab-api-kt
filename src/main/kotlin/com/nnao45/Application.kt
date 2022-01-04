package com.nnao45

import com.nnao45.infra.repo.BenchmarkRepoImpl
import com.nnao45.infra.repo.FibonacchiRepoImpl
import com.nnao45.plugins.configureDDL
import com.nnao45.plugins.configureRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0") {
        install(CallLogging)
        configureRouting(FibonacchiRepoImpl, BenchmarkRepoImpl)
        configureDDL()
    }.start(wait = true)
}
