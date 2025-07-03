package utils

import constants.LentOrBorrowed
import constants.SplitType
import data.model.SpendWithSplit

fun SpendWithSplit.calculateOwes(userId: String): Double {
    val lenders = splits.filter { it.lentOrBorrowed.toInt() == LentOrBorrowed.LENT }
    val borrowers = splits.filter { it.lentOrBorrowed.toInt() == LentOrBorrowed.BORROWED }
    val currentUserLentValue =
        lenders.find { it.userId == userId }?.value_ ?: 0.0
    val currentUserBorrowedValue =
        borrowers.find { it.userId == userId }?.value_ ?: 0.0
    //negative owe means other members owe to the user
    return when (borrowers[0].splitType.toInt()) {
        SplitType.EQUAL -> {
            val sharePerHead = spend.totalAmount / borrowers.size
            if (currentUserLentValue > 0) {
                sharePerHead - currentUserLentValue
            } else {
                sharePerHead
            }
        }

        SplitType.AMOUNT -> currentUserBorrowedValue

        SplitType.SHARE -> {
            val totalShares =
                borrowers.sumOf { it.value_ }
            val costPerShare = spend.totalAmount / totalShares
            if (currentUserLentValue > 0) {
                costPerShare * currentUserBorrowedValue - currentUserLentValue
            } else {
                costPerShare * currentUserBorrowedValue
            }
        }

        SplitType.RATIO -> spend.totalAmount * currentUserBorrowedValue

        SplitType.DIFFERENCE -> {
            val difference = borrowers.sumOf { it.value_ }
            val sharePerHeadWithoutDifference =
                (spend.totalAmount - difference) / borrowers.size
            if (currentUserLentValue > 0) {
                sharePerHeadWithoutDifference + currentUserBorrowedValue - currentUserLentValue
            } else {
                sharePerHeadWithoutDifference + currentUserBorrowedValue
            }
        }

        else -> 0.0
    }
}