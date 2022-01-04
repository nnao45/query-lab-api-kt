package com.nnao45.plugins

import com.nnao45.infra.logger.logger
import com.nnao45.infra.repo.BenchmarkRepoImpl
import com.nnao45.infra.repo.FibonacchiRepoImpl
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting(repo: FibonacchiRepoImpl) {
    logger.info("start routing!")

    // Starting point for a Ktor app:
    routing {
        get("/hc") {
            call.respondText("OK")
        }

        get("/exec") {
            val param: Long =  call.request.queryParameters["n"]?.toLong() ?: 10
            call.respondText("${repo.exec(param)}")
        }
    }
}
