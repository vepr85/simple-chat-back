package com.example.simplechat.chat.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("app.chat")
data class ChatEntity(
    val creatorId: Long,
    @Id val id: Long = 0,
)
