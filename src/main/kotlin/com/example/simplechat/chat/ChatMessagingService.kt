package com.example.simplechat.chat

import com.example.simplechat.chat.entity.MessageEntity
import com.example.simplechat.chat.exception.UserNotInChatException
import com.example.simplechat.chat.model.Message
import com.example.simplechat.chat.repository.MessageRepository
import com.example.simplechat.util.executeAndAwaitNotNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator

@Service
class ChatMessagingService(
    private val chatService: ChatService,
    private val messageRepository: MessageRepository,
    private val transactionalOperator: TransactionalOperator,
) {
    suspend fun loadHistory(
        chatId: Long, userId: Long,
    ): Flow<Message> = transactionalOperator.executeAndAwaitNotNull {
        val chat = chatService.getChat(chatId)
        if (!chat.userIds.contains(userId)) throw UserNotInChatException()

        messageRepository.findAllByChatId(chatId).map { it.toModel() }
    }


    suspend fun sendMessage(
        chatId: Long, userId: Long, message: String,
    ): Message = transactionalOperator.executeAndAwaitNotNull {
        val chat = chatService.getChat(chatId)
        if (!chat.userIds.contains(userId)) throw UserNotInChatException()

        val messageEntity = messageRepository.save(
            MessageEntity(chatId = chatId, senderId = userId, message = message)
        )

        messageEntity.toModel()
    }
}