package com.example.simplechat.chat.entity

import org.springframework.data.relational.core.mapping.Table

@Table("app.chat_user")
data class ChatUserEntity(
    val userId: Long,
    val chatId: Long,
)