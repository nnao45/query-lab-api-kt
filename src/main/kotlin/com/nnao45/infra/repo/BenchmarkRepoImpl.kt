package com.nnao45.infra.repo

import com.nnao45.domain.dao.user.UserDao
import com.nnao45.domain.model.user.User
import com.nnao45.infra.mysql.utils.execAndMap
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object BenchmarkRepoImpl {
    fun exec(): Long = transaction {
        """
            with fp as (
                select user_id as fp_user_id, latitude, longitude
                from foot_stamp
            )
            select count(*) as count
                from post p
                    inner join fp on p.user_id = fp.fp_user_id
                where fp.latitude between -1 and 1
                and fp.longitude between -1 and 1
            ;
            """.trimIndent().execAndMap {
            it.getLong("count")
        }[0]
    }
}