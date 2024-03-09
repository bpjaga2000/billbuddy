package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.ProfileDto
import dev.bpj4.billbuddy.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
        private val searchService: SearchService
) {

    @GetMapping
    fun searchUsers(@RequestParam(name = "query") query: String): ResponseEntity<List<ProfileDto>> {
        return ResponseEntity(searchService.search(query), HttpStatus.OK)
    }

}