package com.nnao45.domain.model.foot_stamp

import com.nnao45.domain.model.user.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object FootStamp: IntIdTable("foot_stamp") {
    val user_id = entityId("user_id", User).index("idx_user_id") references User.id
    val latitude  = double("latitude")
    val longitude  = double("longitude")
    val created_at  = datetime("created_at")
}