package dev.bpj4.billbuddy.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtGenerator: JwtGenerator

    @Autowired
    lateinit var userDetailsService: AuthService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        request.getHeader("Authentication").takeIf { !it.isNullOrBlank() }?.drop(7)?.let { token ->
            if (jwtGenerator.validateToken(token)) {
                val userDetails = userDetailsService.loadUserByUsername(jwtGenerator.getUserNameFromJwt(token))
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            } else {
                throw AuthenticationCredentialsNotFoundException("Token Expired/Invalid")
            }
        }

        return filterChain.doFilter(request, response)
    }
}