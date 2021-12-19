package com.nnao45.infra.repo

import com.nnao45.domain.dao.user.UserDao
import com.nnao45.domain.model.user.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepoImpl {
    fun getUser(): Long = transaction {
        User.selectAll().count()
    }
}