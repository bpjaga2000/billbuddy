package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.SyncDto

interface SyncService {
    fun fetchAllData(id: String): SyncDto
    fun fetchData(id: String, timeInSecs: Long): SyncDto

}