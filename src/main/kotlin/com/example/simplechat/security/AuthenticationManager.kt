package com.example.simplechat.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(
    val tokenService: TokenService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials as String
        return Mono.fromCallable {
            tokenService.decode(authToken)
        }.map {
            UsernamePasswordAuthenticationToken.authenticated(it, null, listOf())
        }
    }
}