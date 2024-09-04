package data.model.dto

data class UserIdListDto(
        val ids: List<String> = listOf(),
        val requesterId: String
)