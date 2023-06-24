package com.example.simplechat.chat.io

import com.example.simplechat.chat.io.dto.ChatDTO
import com.example.simplechat.chat.model.Chat

fun Chat.toDto() = ChatDTO(
    id = id,
    creatorId = creatorId,
    userIds = userIds
)