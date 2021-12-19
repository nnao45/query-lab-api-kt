package com.nnao45.plugins

import com.nnao45.domain.model.foot_stamp.FootStamp
import com.nnao45.domain.model.post.Post
import com.nnao45.domain.model.user.User
import com.nnao45.infra.logger.logger
import com.nnao45.infra.mysql.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDDL() {
    logger.info("setup mysql!")
    initMysqlClient()
    transaction {
        SchemaUtils.create(
            User,
            Post,
            FootStamp
        )
    }
}