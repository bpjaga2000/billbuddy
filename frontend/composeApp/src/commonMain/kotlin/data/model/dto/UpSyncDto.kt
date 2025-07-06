package data.model.dto

import data.model.dto.SpendDto
import data.model.dto.SpendSplitDto

data class UpSyncDto(
    val spends: List<SpendDto>,
    val spendSplit: List<SpendSplitDto>,
    val groupSettles: List<GroupSettlesDto>
)