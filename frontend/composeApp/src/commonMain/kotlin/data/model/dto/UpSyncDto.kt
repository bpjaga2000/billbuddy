package data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpSyncDto(
    val spends: List<SpendDto>,
    val spendSplit: List<SpendSplitDto>,
    val groupSettles: List<GroupSettlesDto>
)