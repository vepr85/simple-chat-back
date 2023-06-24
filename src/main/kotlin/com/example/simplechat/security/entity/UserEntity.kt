package com.example.simplechat.security.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("app.user")
data class UserEntity(
    val email: String,
    val passwordHash: String,
    @Id val id: Long = 0,
)