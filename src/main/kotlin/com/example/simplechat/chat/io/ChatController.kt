package com.example.simplechat.chat.io

import com.example.simplechat.chat.ChatMessagingService
import com.example.simplechat.chat.ChatService
import com.example.simplechat.chat.io.dto.ChatDTO
import com.example.simplechat.chat.io.dto.MessageDTO
import com.example.simplechat.security.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val chatService: ChatService,
    private val chatMessagingService: ChatMessagingService,
) {
    @GetMapping("/chat/test")
    fun test(
        @AuthenticationPrincipal user: AuthUser,
    ): String {
        return "test string"
    }

    @PostMapping("/chat")
    suspend fun createChat(
        @AuthenticationPrincipal user: AuthUser,
    ): ChatDTO {
        return chatService.createChat(user.id).toDto()
    }

    @PostMapping("/chat/invite")
    suspend fun invite(
        @RequestParam chatId: Long,
        @RequestParam userId: Long,
        @AuthenticationPrincipal user: AuthUser,
    ): ChatDTO {
        return chatService.addUser(
            chatId = chatId,
            ownerId = user.id,
            userId = userId,
        ).toDto()
    }

    @PostMapping("/chat/send-message")
    suspend fun sendMessage(
        @RequestParam chatId: Long,
        @RequestBody message: String,
        @AuthenticationPrincipal user: AuthUser,
    ): MessageDTO {
        return chatMessagingService.sendMessage(
            chatId = chatId, userId = user.id, message = message
        ).toDto()
    }

    @GetMapping("/chat/history")
    suspend fun sendMessage(
        @RequestParam chatId: Long,
        @AuthenticationPrincipal user: AuthUser,
    ): Flow<MessageDTO> {
        return chatMessagingService.loadHistory(
            chatId = chatId, userId = user.id
        ).map { it.toDto() }
    }
}