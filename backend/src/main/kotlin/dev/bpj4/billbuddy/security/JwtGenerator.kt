package dev.bpj4.billbuddy.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


@Component
class JwtGenerator { //TODO remove deprecated methods

    fun generateJwToken(authentication: Authentication): String = Jwts
            .builder()
            .issuer(MethodHandles.lookup().lookupClass().getPackage().name)
            .issuedAt(Date())
            .expiration(Date(Date().time + SecurityConstants.EXPIRATION))
            .subject(authentication.name)
            .signWith(SignatureAlgorithm.HS256, "SECURITYTOKENBILLBUDDYSECURITYTOKENBILLBUDDY")
            .compact()

    fun getUserNameFromJwt(token: String) = Jwts.parser().setSigningKey("SECURITYTOKENBILLBUDDYSECURITYTOKENBILLBUDDY").build()
            .parseClaimsJwt(token).body.subject

    fun validateToken(token: String): Boolean {
        try {
            return Jwts.parser().setSigningKey("SECURITYTOKENBILLBUDDYSECURITYTOKENBILLBUDDY").build()
                    .parseClaimsJwt(token).body.let {
                it.issuer == MethodHandles.lookup().lookupClass().getPackage().name &&
                        it.expiration < Date()
            }
        } catch (e: Exception) {
            throw AuthenticationCredentialsNotFoundException("Token expired/invalid")
        }
    }
}