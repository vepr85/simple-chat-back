package com.example.simplechat.security.entity

import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("app.refresh_token")
data class RefreshTokenEntity(
    val userId: Long,
    val token: String,
    val createdAt: Instant,
    val expiredAt: Instant,
)