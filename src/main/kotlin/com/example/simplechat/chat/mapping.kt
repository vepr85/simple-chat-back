package com.example.simplechat.chat

import com.example.simplechat.chat.entity.ChatEntity
import com.example.simplechat.chat.entity.MessageEntity
import com.example.simplechat.chat.model.Chat
import com.example.simplechat.chat.model.Message

fun ChatEntity.toModel(userIds: Set<Long>) = Chat(
    id = id,
    creatorId = creatorId,
    userIds = userIds
)

fun MessageEntity.toModel() = Message(
    chatId = chatId,
    senderId = senderId,
    message = message,
    createdAt = createdAt,
    id = id,
)