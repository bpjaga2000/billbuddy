package data.model

data class Balance(
    var userSettles: Map<String, Balance>?,
    val userId: String,
    val name: String,
    var amount: Double,
    val groupId: String?
)
