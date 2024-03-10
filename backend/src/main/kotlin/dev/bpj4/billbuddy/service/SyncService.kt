package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.SyncDto
import dev.bpj4.billbuddy.dto.UpSyncDto

interface SyncService {
    fun fetchAllData(id: String): SyncDto
    fun fetchData(id: String, timeInSecs: Long): SyncDto
    fun uploadData(id: String, upSyncDto: UpSyncDto): String

}