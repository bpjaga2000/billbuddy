package data.model

import dev.bpj4.billbuddy.tableandmigrations.Groups

data class GroupWithOwes(
    val group: Groups,
    var owe: Double
)