package com.example.simplechat.chat

import com.example.simplechat.chat.entity.ChatEntity
import com.example.simplechat.chat.exception.ChatNotFoundException
import com.example.simplechat.chat.exception.InvalidChatOwnerException
import com.example.simplechat.chat.model.Chat
import com.example.simplechat.chat.repository.ChatRepository
import com.example.simplechat.chat.repository.ChatUserRepository
import com.example.simplechat.util.executeAndAwaitNotNull
import com.example.simplechat.util.transactionRequired
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatUserRepository: ChatUserRepository,
    private val transactionalOperator: TransactionalOperator,
) {
    suspend fun getChat(
        chatId: Long,
    ): Chat = transactionRequired {
        val chat = chatRepository.findById(chatId) ?: throw ChatNotFoundException()
        val chatUsers = chatUserRepository.findAllByChatId(chatId).toList()
        chat.toModel(
            chatUsers.map { it.userId }.toSet()
        )
    }

    suspend fun createChat(
        creatorId: Long,
    ): Chat = transactionalOperator.executeAndAwaitNotNull {
        val chat = chatRepository.save(ChatEntity(creatorId))
        chatUserRepository.create(userId = creatorId, chatId = chat.id)

        chat.toModel(listOf(creatorId).toSet())
    }

    suspend fun addUser(
        chatId: Long,
        ownerId: Long,
        userId: Long,
    ): Chat = transactionalOperator.executeAndAwaitNotNull {
        val chat = chatRepository.findById(chatId) ?: throw ChatNotFoundException()
        if (chat.creatorId != ownerId) throw InvalidChatOwnerException()

        chatUserRepository.create(userId = userId, chatId = chat.id)

        val chatUsers = chatUserRepository.findAllByChatId(chatId).toList()

        chat.toModel(chatUsers.map { it.userId }.toSet())
    }
}