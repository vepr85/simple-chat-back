@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.example.simplechat.security.repository

import com.example.simplechat.security.entity.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : CoroutineCrudRepository<UserEntity, Long> {
    suspend fun countAllByEmail(email: String): Long

    suspend fun findOneByEmail(email: String): UserEntity?
}