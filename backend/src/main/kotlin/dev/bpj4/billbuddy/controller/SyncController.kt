package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.SyncDto
import dev.bpj4.billbuddy.service.SyncService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sync")
class SyncController(
        val syncService: SyncService
) {

    @GetMapping
    fun fetchData(@RequestParam id: String, @RequestParam("lastSyncTime") lastSyncTime: Long?): ResponseEntity<SyncDto> {
        return lastSyncTime?.let {
            ResponseEntity(syncService.fetchData(id, it), HttpStatus.OK)
        } ?: ResponseEntity(syncService.fetchAllData(id), HttpStatus.OK)
    }

}