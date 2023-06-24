package com.example.simplechat.chat.repository

import com.example.simplechat.chat.entity.ChatEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : CoroutineCrudRepository<ChatEntity, Long>