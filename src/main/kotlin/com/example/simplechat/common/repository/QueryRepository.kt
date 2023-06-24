package com.example.simplechat.common.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

@Suppress("unused")
@NoRepositoryBean
interface QueryRepository<T> : Repository<T, Void> {
    fun findAll(): Flow<T>

    suspend fun count(): Long
}