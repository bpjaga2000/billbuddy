package data.model.dto

import data.GroupTags

data class GroupDto(
    val name:String,
    val tag: GroupTags,
    val userId: String
)