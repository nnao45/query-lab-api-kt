package com.nnao45.infra.logger

import mu.KotlinLogging
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext

val logger by lazy { KotlinLogging.logger {}  }

object DoNotLogger: SqlLogger {
    override fun log(__context: StatementContext, __transaction: Transaction) {
        // do nothing
        return
    }
}