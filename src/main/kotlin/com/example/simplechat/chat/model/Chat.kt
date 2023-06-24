package com.example.simplechat.chat.model

@JvmRecord
data class Chat(
    val id: Long,
    val creatorId: Long,
    val userIds: List<Long>,
) : java.io.Serializable