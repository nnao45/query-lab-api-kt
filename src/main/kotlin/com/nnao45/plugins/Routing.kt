package com.nnao45.plugins

import com.nnao45.infra.logger.logger
import com.nnao45.infra.repo.BenchmarkRepoImpl
import com.nnao45.infra.repo.UserRepoImpl
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.LocalDateTime

fun Application.configureRouting(repo: BenchmarkRepoImpl) {
    logger.info("start routing!")

    // Starting point for a Ktor app:
    routing {
        get("/") {
            call.respondText("${LocalDateTime.now()}")
        }

        get("/exec") {
            call.respondText("${repo.exec()}")
        }
    }
}
