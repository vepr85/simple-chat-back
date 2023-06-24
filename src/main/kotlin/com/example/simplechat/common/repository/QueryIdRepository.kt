package com.example.simplechat.common.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

@Suppress("unused")
@NoRepositoryBean
interface QueryIdRepository<T, ID> : Repository<T, ID> {
    suspend fun findById(id: ID): T?

    suspend fun existsById(id: ID): Boolean

    fun findAll(): Flow<T>

    fun findAllById(ids: Iterable<ID>): Flow<T>

    fun findAllById(ids: Flow<ID>): Flow<T>

    suspend fun count(): Long
}