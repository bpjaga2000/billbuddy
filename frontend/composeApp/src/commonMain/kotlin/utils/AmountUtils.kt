package utils

import constants.SplitType
import data.model.SpendWithSplit

fun SpendWithSplit.calculateOwes(userId: String): Double {
    val currentUserValue =
        splits.find { it.userId == userId }?.value_ ?: 0.0
    //negative owe means other members owe to the user
    return when (splits[0].splitType.toInt()) {
        SplitType.EQUAL -> {
            val sharePerHead = spend.totalAmount / splits.size
            if (userId == spend.spentBy) {
                sharePerHead - spend.totalAmount
            } else {
                sharePerHead
            }
        }

        SplitType.AMOUNT -> currentUserValue

        SplitType.SHARE -> {
            val totalShares =
                splits.sumOf { it.value_ }
            val costPerShare = spend.totalAmount / totalShares
            if (userId == spend.spentBy) {
                costPerShare * currentUserValue - spend.totalAmount
            } else {
                costPerShare * currentUserValue
            }
        }

        SplitType.RATIO -> spend.totalAmount * currentUserValue

        SplitType.DIFFERENCE -> {
            val difference = splits.sumOf { it.value_ }
            val sharePerHeadWithoutDifference =
                (spend.totalAmount - difference) / splits.size
            if (userId == spend.spentBy) {
                spend.totalAmount - sharePerHeadWithoutDifference - currentUserValue
            } else {
                sharePerHeadWithoutDifference + currentUserValue
            }
        }

        else -> 0.0
    }
}