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
                         inner join user u2 on foot_stamp.user_id = u2.id
            )
               , test1 as (
                select latitude as test1_latitude, longitude as test1_longitude
                from post p
                         inner join fp on p.user_id = fp.fp_user_id
                where fp.latitude between -20 and 30
                  and p.user_id between 1 and 20000
                  and fp.longitude between -20 and 30
                  and p.created_at between '1990-11-01' and '2002-01-01'
            )
               , test2 as (
                select latitude as test2_latitude, longitude as test2_longitude
                from post p
                         inner join fp on p.user_id = fp.fp_user_id
                where fp.latitude between -50 and 10
                  and p.user_id between 100 and 3000
                  and fp.longitude between -20 and 30
                  and p.created_at between '1990-11-01' and '2002-01-01'
            )
            select count(*) as count
            from fp
                     inner join test1 t1 on fp.latitude = t1.test1_latitude;
            """.trimIndent().execAndMap {
            it.getLong("count")
        }[0]
    }
}