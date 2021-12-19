package com.nnao45.domain.dao.post

import com.nnao45.domain.model.post.Post
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PostDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<PostDao>(Post)
    val title by Post.title
    val body by Post.body
    val user_id by Post.user_id
    val published by Post.published
    val created_at by Post.created_at
}