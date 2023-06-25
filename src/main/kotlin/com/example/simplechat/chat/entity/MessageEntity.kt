package com.example.simplechat.chat.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("app.message")
data class MessageEntity(
    val chatId: Long,
    val senderId: Long,
    val message: String,
    val createdAt: Instant = Instant.now(),
    @Id val id: Long = 0,
)
