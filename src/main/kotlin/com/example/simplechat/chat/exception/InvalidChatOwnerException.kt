package com.example.simplechat.chat.exception

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class InvalidChatOwnerException : RuntimeException("Invalid chat owner")