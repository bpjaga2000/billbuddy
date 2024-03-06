package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.LoginDto
import dev.bpj4.billbuddy.dto.LogoutDto
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
        private val authenticationService: AuthenticationService
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
        } ?: ResponseEntity("Username does not exist", HttpStatus.BAD_REQUEST)
    }

    @PostMapping("logout")
    fun logout(@RequestBody logoutDto: LogoutDto): ResponseEntity<Any> {
        return authenticationService.logoutUser(logoutDto)?.let {
            ResponseEntity("Logged out successfully", HttpStatus.CREATED)
        } ?: ResponseEntity("Invalid User", HttpStatus.BAD_REQUEST)
    }

}