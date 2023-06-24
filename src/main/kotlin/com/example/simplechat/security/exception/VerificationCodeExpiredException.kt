package com.example.simplechat.security.exception

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class VerificationCodeExpiredException : AuthenticationException("Verification code expired")