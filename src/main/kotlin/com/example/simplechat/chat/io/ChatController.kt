package com.example.simplechat.chat.io

import com.example.simplechat.chat.ChatService
import com.example.simplechat.chat.io.dto.ChatDTO
import com.example.simplechat.security.AuthUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    val chatService: ChatService,
) {
    @GetMapping("/chat")
    fun test(): String {
        return "ok"
    }

    @PostMapping("/chat")
    suspend fun createChat(
        @AuthenticationPrincipal user: AuthUser,
    ): ChatDTO {
        return chatService.createChat(user.id).toDto()
    }
}