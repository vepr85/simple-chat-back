package com.example.simplechat.security.io

import com.example.simplechat.security.AuthService
import com.example.simplechat.security.exception.*
import com.example.simplechat.security.io.dto.SignInRequestDTO
import com.example.simplechat.security.io.dto.SignUpRequestDTO
import com.example.simplechat.security.io.dto.TokenResponseDTO
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-in", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(UserNotFoundException::class, PasswordsDoesNotMatchException::class)
    suspend fun signIn(@RequestBody @Valid req: SignInRequestDTO): TokenResponseDTO {
        return authService.signIn(req.email, req.password).toDto()
    }

    @PostMapping("/sign-up", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(EmailAlreadyUsedException::class)
    suspend fun signUp(@RequestBody @Valid req: SignUpRequestDTO): TokenResponseDTO {
        return authService.signUp(req.email, req.password).toDto()
    }

    private fun AuthService.TokenPair.toDto() =
        TokenResponseDTO(authToken = authToken, refreshToken = refreshToken)
}