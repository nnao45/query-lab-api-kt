package com.nnao45.plugins

import com.nnao45.infra.logger.logger
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.LocalDateTime

fun Application.configureRouting() {
    logger.info("start routing!")

    // Starting point for a Ktor app:
    routing {
        get("/") {
            call.respondText("${LocalDateTime.now()}")
        }
    }
    routing {
    }
}
