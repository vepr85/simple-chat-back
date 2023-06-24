package com.example.simplechat.security

import com.example.simplechat.security.exception.ValidationException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Instant

@Component
@Order(-2)
class GlobalExceptionHandler(
    private val env: Environment,
    private val objectMapper: ObjectMapper,
) : ErrorWebExceptionHandler {
    override fun handle(serverWebExchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val path = serverWebExchange.request.path.toString()
        val printStackTrace = env.activeProfiles.any { it == "local" }
        return writeResponse(
            serverWebExchange, exceptionToErrorResponse(ex, path, printStackTrace)
        )
    }

    private fun writeResponse(serverWebExchange: ServerWebExchange, value: Error): Mono<Void> {
        return Mono.defer { Mono.just(serverWebExchange.response) }.flatMap { response ->
            response.setRawStatusCode(value.status)
            response.headers.contentType = MediaType.APPLICATION_JSON
            val dataBufferFactory = response.bufferFactory()
            val buffer = dataBufferFactory.wrap(objectMapper.writeValueAsBytes(value))
            response.writeWith(Mono.just(buffer)).doOnError { DataBufferUtils.release(buffer) }
        }
    }

    companion object {
        private fun exceptionToErrorResponse(
            ex: Throwable,
            path: String,
            printStackTraceAlways: Boolean,
        ): Error {
            if (printStackTraceAlways) {
                ex.printStackTrace()
            }

            val exception = if (ex is WebExchangeBindException) {
                webExchangeBindExceptionConverter(ex)
            } else {
                ex
            }

            val responseStatus = when (exception) {
                is AuthenticationException -> HttpStatus.UNAUTHORIZED
                is AccessDeniedException -> HttpStatus.FORBIDDEN
                is ErrorResponse -> HttpStatus.resolve(exception.statusCode.value())
                else -> (exception::class.annotations.find { it is ResponseStatus } as? ResponseStatus)?.value
            } ?: HttpStatus.INTERNAL_SERVER_ERROR

            if (!printStackTraceAlways && responseStatus.is5xxServerError) {
                ex.printStackTrace()
            }

            return Error(
                Instant.now().toEpochMilli(),
                path,
                responseStatus.value(),
                exception.javaClass.simpleName,
                exception.message?.ifBlank { null } ?: responseStatus.reasonPhrase,
            )
        }

        private fun webExchangeBindExceptionConverter(exception: WebExchangeBindException): Throwable {
            val message = exception.fieldErrors.mapNotNull { it.defaultMessage }.ifEmpty { null }?.joinToString(", ")
                ?.ifBlank { null }
            if (exception.reason == "Validation failure" && message != null) {
                return ValidationException(message)
            }
            return RuntimeException()
        }

        @JvmRecord
        data class Error(
            val timestamp: Long,
            val path: String,
            val status: Int,
            val error: String,
            val message: String,
        )
    }
}