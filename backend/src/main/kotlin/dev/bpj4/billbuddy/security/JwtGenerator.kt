package dev.bpj4.billbuddy.security

import io.jsonwebtoken.Jwts
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles
import java.util.*
import javax.crypto.spec.SecretKeySpec


@Component
class JwtGenerator {

    val key = SecretKeySpec(
            "SECURITYSECURITYSECURITYSECURITY".toByteArray(),
            "HmacSHA256"
    )//todo try Asymmetric

    fun generateJwToken(authentication: Authentication): String = Jwts
            .builder()
            .issuer(MethodHandles.lookup().lookupClass().getPackage().name)
            .issuedAt(Date())
            .expiration(Date(Date().time + SecurityConstants.EXPIRATION))
            .subject(authentication.name)
            .signWith(key)
            .compact()

    fun getUserNameFromJwt(token: String): String? = Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).payload.subject

    fun validateToken(token: String): Boolean {
        try {
            return Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).payload.let {
                        it.issuer == MethodHandles.lookup().lookupClass().getPackage().name &&
                                it.expiration < Date()
                    }
        } catch (e: Exception) {
            throw AuthenticationCredentialsNotFoundException("Token expired/invalid")
        }
    }
}