package com.example.simplechat.chat

import com.example.simplechat.chat.entity.ChatEntity
import com.example.simplechat.chat.model.Chat

fun ChatEntity.toModel(userIds: List<Long>) = Chat(
    id = id,
    creatorId = creatorId,
    userIds = userIds
)