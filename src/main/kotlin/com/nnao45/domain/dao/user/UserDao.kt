package com.nnao45.domain.dao.user

import com.nnao45.domain.model.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserDao>(User)
    val name by User.name
    val description by User.description
    val created_at by User.created_at
}