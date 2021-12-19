package com.nnao45.domain.model.post

import com.nnao45.domain.model.user.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Post: IntIdTable("post") {
    val title = varchar("title", 255)
    val body = text("body")
    val user_id = entityId("user_id", User).index("idx_user_id") references User.id
    val published  = bool("published")
    val created_at  = datetime("created_at")
}