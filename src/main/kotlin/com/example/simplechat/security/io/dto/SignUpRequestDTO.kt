package com.example.simplechat.security.io.dto

import jakarta.validation.constraints.Email

class SignUpRequestDTO(
    @field:Email
    val email: String,
    val password: String,
)