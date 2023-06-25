package com.example.simplechat.chat.io.dto

import java.time.Instant

class MessageDTO(
    val chatId: Long,
    val senderId: Long,
    val message: String,
    val createdAt: Instant,
    val id: Long,
)