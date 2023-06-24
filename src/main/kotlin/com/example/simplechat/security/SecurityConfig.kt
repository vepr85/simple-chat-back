package com.example.simplechat.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig {
    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        authenticationManager: AuthenticationManager,
        authenticationConverter: AuthenticationConverter,
        globalExceptionHandler: GlobalExceptionHandler,
    ): SecurityWebFilterChain {
        return http {
            authorizeExchange {
                authorize("/chat", authenticated)
                authorize("/**", permitAll)
            }

            addFilterAt(AuthenticationWebFilter(authenticationManager).apply {
                setServerAuthenticationConverter(authenticationConverter)
                setAuthenticationFailureHandler { wfe, ex ->
                    globalExceptionHandler.handle(wfe.exchange, ex)
                }
            }, SecurityWebFiltersOrder.AUTHENTICATION)

            exceptionHandling {
                authenticationEntryPoint = ServerAuthenticationEntryPoint { exchange, ex ->
                    globalExceptionHandler.handle(exchange, ex)
                }
                accessDeniedHandler = ServerAccessDeniedHandler { exchange, ex ->
                    globalExceptionHandler.handle(exchange, ex)
                }
            }

            formLogin { disable() }
            httpBasic { disable() }
            logout { disable() }
            cors {
                configurationSource = corsConfigurationSource()
            }
            csrf { disable() }
        }
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("*") // TODO: replace with exact origin
            maxAge = 8000L
            addAllowedMethod(CorsConfiguration.ALL)
            addAllowedHeader(CorsConfiguration.ALL)
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
