package com.nnao45.plugins

import com.nnao45.domain.model.foot_stamp.FootStamp
import com.nnao45.domain.model.post.Post
import com.nnao45.domain.model.user.User
import com.nnao45.infra.logger.logger
import com.nnao45.infra.mysql.initMysqlClient
import com.nnao45.infra.mysql.utils.execAndMap
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

private fun doDDL(sql: String) {
    transaction {
        sql.execAndMap { it.getBytes(1) }
    }
}

fun configureDDL() {
    logger.info("setup mysql!")
    initMysqlClient()
    doDDL("CREATE DATABASE IF NOT EXISTS ${System.getenv("MYSQL_DB") ?: "jq_api"} DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    transaction {
        SchemaUtils.create(
            User,
            Post,
            FootStamp
        )
    }
}