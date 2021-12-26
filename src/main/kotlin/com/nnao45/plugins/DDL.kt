package com.nnao45.plugins

import com.nnao45.domain.model.foot_stamp.FootStamp
import com.nnao45.domain.model.post.Post
import com.nnao45.domain.model.user.User
import com.nnao45.infra.logger.DoNotLogger
import com.nnao45.infra.logger.logger
import com.nnao45.infra.mysql.initMysqlClient
import com.nnao45.infra.mysql.utils.execAndMap
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

fun configureDDL() {
    logger.info("setup mysql!")
    initMysqlClient()
    transaction {
        "CREATE DATABASE IF NOT EXISTS ${System.getenv("MYSQL_DB") ?: "jq_api"} DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci"
            .execAndMap { it.getBytes(1) }
    }
    transaction {
        SchemaUtils.create(
            User,
            Post,
            FootStamp
        )
    }
    transaction(
    ) {
        if (0L == User.selectAll().count()) {
            val insertSQLString = File("src/main/resources/data/fulldb-11-12-2021-15-57-beta.sql").readText(UTF_8)
            insertSQLString.execAndMap { it.getBytes(1) }
        }
    }
}