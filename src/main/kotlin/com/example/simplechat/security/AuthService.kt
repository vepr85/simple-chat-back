package com.example.simplechat.security

import com.example.simplechat.security.entity.UserEntity
import com.example.simplechat.security.exception.EmailAlreadyUsedException
import com.example.simplechat.security.exception.PasswordsDoesNotMatchException
import com.example.simplechat.security.exception.UserNotFoundException
import com.example.simplechat.security.repository.UserRepository
import com.example.simplechat.util.executeAndAwaitNotNull
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator

@Service
class AuthService(
    private val tokenService: TokenService,
    private val transactionalOperator: TransactionalOperator,
    private val userRepository: UserRepository,
) {
    suspend fun signIn(email: String, password: String): TokenPair = transactionalOperator.executeAndAwaitNotNull {
        val user = userRepository.findOneByEmail(email) ?: throw UserNotFoundException()
        if (user.passwordHash != password) {
            throw PasswordsDoesNotMatchException()
        }
        val token = tokenService.sign(AuthUser(user.id, user.email))
        TokenPair(token, "")
    }

    suspend fun signUp(email: String, password: String): TokenPair = transactionalOperator.executeAndAwaitNotNull {
        if (userRepository.countAllByEmail(email) > 0) {
            throw EmailAlreadyUsedException()
        }
        val user = userRepository.save(UserEntity(email, password))
        val token = tokenService.sign(AuthUser(user.id, user.email))
        TokenPair(token, "")
    }

    class TokenPair(
        val authToken: String,
        val refreshToken: String,
    )
}
