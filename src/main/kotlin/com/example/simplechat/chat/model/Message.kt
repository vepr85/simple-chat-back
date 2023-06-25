package com.example.simplechat.chat.model

import java.time.Instant

@JvmRecord
data class Message(
    val chatId: Long,
    val senderId: Long,
    val message: String,
    val createdAt: Instant,
    val id: Long,
) : java.io.Serializable