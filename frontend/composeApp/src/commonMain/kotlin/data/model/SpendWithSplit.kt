package data.model

import dev.bpj4.billbuddy.tableandmigrations.SpendSplits
import dev.bpj4.billbuddy.tableandmigrations.Spends

data class SpendWithSplit(
    val spend: Spends,
    val splits: List<SpendSplits>
)