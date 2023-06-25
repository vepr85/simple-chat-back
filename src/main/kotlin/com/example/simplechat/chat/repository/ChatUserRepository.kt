@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.example.simplechat.chat.repository

import com.example.simplechat.chat.entity.ChatUserEntity
import com.example.simplechat.common.repository.QueryRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChatUserRepository : QueryRepository<ChatUserEntity> {
    fun findAllByChatId(chatId: Long): Flow<ChatUserEntity>

    @Query("insert into app.chat_user(chat_id, user_id) values (:chatId, :userId)")
    @Modifying
    suspend fun create(chatId: Long, userId: Long)
}