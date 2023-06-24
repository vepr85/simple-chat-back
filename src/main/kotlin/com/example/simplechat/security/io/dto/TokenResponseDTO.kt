package com.example.simplechat.security.io.dto

data class TokenResponseDTO(
    val authToken: String,
    val refreshToken: String,
)