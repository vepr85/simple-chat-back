package com.example.simplechat.security

import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationConverter : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { it.startsWith(TOKEN_PREFIX) }
            .map {
                val authToken = it.removePrefix(TOKEN_PREFIX)
                PreAuthenticatedAuthenticationToken(null, authToken)
            }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}