package data.database.entity

data class BaseEntity(
    var id: String,
    var createdAtFrontend: Long,
    var updatedAtFrontend: Long,
    var deletedAtFrontend: Long,
    var createdAt: Long,
    var updatedAt: Long,
    var deletedAt: Long?
)