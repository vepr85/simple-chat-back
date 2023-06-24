package com.example.simplechat.chat

import com.example.simplechat.chat.entity.ChatEntity
import com.example.simplechat.chat.model.Chat
import com.example.simplechat.chat.repository.ChatRepository
import com.example.simplechat.chat.repository.ChatUserRepository
import com.example.simplechat.util.executeAndAwaitNotNull
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatUserRepository: ChatUserRepository,
    private val transactionalOperator: TransactionalOperator,
) {
    suspend fun createChat(
        creatorId: Long,
    ): Chat = transactionalOperator.executeAndAwaitNotNull {
        val chat = chatRepository.save(ChatEntity(creatorId))
        chatUserRepository.create(userId = creatorId, chatId = chat.id)

        chat.toModel(listOf(creatorId))
    }

}