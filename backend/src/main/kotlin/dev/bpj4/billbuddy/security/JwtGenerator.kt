package dev.bpj4.billbuddy.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles
import java.util.*

@Component
class JwtGenerator {

    fun generateJwToken(authentication: Authentication): String = Jwts
            .builder()
            .setIssuer(MethodHandles.lookup().lookupClass().getPackage().name)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + SecurityConstants.EXPIRATION))
            .setSubject(authentication.name)
            .signWith(SignatureAlgorithm.HS256, "SECURITY_TOKEN")
            .compact()

    fun getUserNameFromJwt(token: String) = Jwts.parser().parseClaimsJwt(token).body.subject

    fun validateToken(token: String): Boolean {
        try {
            return Jwts.parser().setSigningKey("SECURITY_TOKEN").parseClaimsJwt(token).body.let {
                it.issuer == MethodHandles.lookup().lookupClass().getPackage().name &&
                        it.expiration < Date()
            }
        } catch (e: Exception) {
            throw AuthenticationCredentialsNotFoundException("Token expired/invalid")
        }
    }
}