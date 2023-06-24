package com.example.simplechat.chat.io.dto

class ChatDTO(
    val id: Long,
    val creatorId: Long,
    val userIds: List<Long>,
)