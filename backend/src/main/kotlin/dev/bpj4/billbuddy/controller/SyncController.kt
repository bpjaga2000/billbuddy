package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.SyncDto
import dev.bpj4.billbuddy.dto.UpSyncDto
import dev.bpj4.billbuddy.service.SyncService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PostMapping
    fun uploadData(@RequestParam id: String, @RequestBody upSyncDto: UpSyncDto): ResponseEntity<String> {
        return ResponseEntity(syncService.uploadData(id, upSyncDto), HttpStatus.OK)
    }

}