package com.example.simplechat.chat.io

import com.example.simplechat.chat.io.dto.ChatDTO
import com.example.simplechat.chat.io.dto.MessageDTO
import com.example.simplechat.chat.model.Chat
import com.example.simplechat.chat.model.Message

fun Chat.toDto() = ChatDTO(
    id = id,
    creatorId = creatorId,
    userIds = userIds
)

fun Message.toDto() = MessageDTO(
    chatId = chatId,
    senderId = senderId,
    message = message,
    createdAt = createdAt,
    id = id,
)