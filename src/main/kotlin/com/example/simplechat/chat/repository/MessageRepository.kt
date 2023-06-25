@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.example.simplechat.chat.repository

import com.example.simplechat.chat.entity.MessageEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : CoroutineCrudRepository<MessageEntity, Long> {
    fun findAllByChatId(chatId: Long): Flow<MessageEntity>
}