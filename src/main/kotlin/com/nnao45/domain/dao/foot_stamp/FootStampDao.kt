package com.nnao45.domain.dao.foot_stamp

import com.nnao45.domain.model.foot_stamp.FootStamp
import com.nnao45.domain.model.post.Post
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FootStampDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<FootStampDao>(FootStamp)
    val user_id by FootStamp.user_id
    val latitude by FootStamp.latitude
    val longitude by FootStamp.longitude
    val created_at by FootStamp.created_at
}