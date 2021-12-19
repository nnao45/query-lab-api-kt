package com.nnao45.domain.model.user

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object User: IntIdTable("user") {
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val created_at  = datetime("created_at")
}
