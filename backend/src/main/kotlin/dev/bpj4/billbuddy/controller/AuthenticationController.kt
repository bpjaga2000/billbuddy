package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.LoginDto
import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
        val authenticationService: AuthenticationService
) {

    @PostMapping("register")
    fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<Any> {
        return authenticationService.createUser(registerDto)?.let {
            ResponseEntity(it, HttpStatus.CREATED)
        } ?: ResponseEntity("Username already taken", HttpStatus.BAD_REQUEST)
    }

    @PostMapping("login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<Any> {
        return authenticationService.verifyUser(loginDto)?.let {
            ResponseEntity(it, HttpStatus.CREATED)
        } ?: ResponseEntity("Username already taken", HttpStatus.BAD_REQUEST)
    }

}