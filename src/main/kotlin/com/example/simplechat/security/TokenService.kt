package com.example.simplechat.security

import com.example.simplechat.security.exception.InvalidTokenException
import com.example.simplechat.security.exception.TokenExpiredException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*


@Service
class TokenService(
    @Value("\${app.security.token-expiration-timeout}")
    private val tokenExpirationTimeout: Long,
    @Value("\${app.security.hmac-secret}")
    private val hmacSecret: String,
) {
    private val signer = MACSigner(hmacSecret)
    private val verifier = MACVerifier(hmacSecret)

    fun sign(user: AuthUser): String {
        val now = Instant.now()
        val claimsSet = JWTClaimsSet.Builder()
            .subject(user.name)
            .claim("id", user.id)
            .issueTime(Date.from(now))
            .expirationTime(Date.from(now.plusMillis(tokenExpirationTimeout)))
            .build()

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimsSet)

        signedJWT.sign(signer)

        return signedJWT.serialize()
    }

    @Suppress("unused")
    fun verify(token: String): Boolean {
        return SignedJWT.parse(token).verify(verifier)
    }

    fun decode(token: String): AuthUser {
        val signedJWT = try {
            SignedJWT.parse(token).apply { verify(verifier) }
        } catch (_: Throwable) {
            throw InvalidTokenException()
        }

        if (signedJWT.jwtClaimsSet.expirationTime.toInstant().isBefore(Instant.now())) {
            throw TokenExpiredException()
        }

        return try {
            val username = signedJWT.jwtClaimsSet.subject!!
            val id = signedJWT.jwtClaimsSet.getClaim("id") as Long
            AuthUser(id, username)
        } catch (_: Throwable) {
            throw InvalidTokenException()
        }
    }
}