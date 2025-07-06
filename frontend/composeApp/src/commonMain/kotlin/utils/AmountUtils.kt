package utils

import constants.SplitType
import data.model.SpendWithSplit
import data.repository.RepositoryImpl
import kotlinx.coroutines.flow.flow

fun SpendWithSplit.calculateOwes(userId: String): Double {
    val currentUserSplitValue =
        splits.find { it.userId == userId }?.value_ ?: 0.0
    //negative owe means other members owe to the user
    return when (splits[0].splitType.toInt()) {
        SplitType.EQUAL -> {
            val userShare = currentUserSplitValue * spend.totalAmount / splits.size
            if (spend.spentBy == userId)
                userShare - spend.totalAmount
            else
                userShare
        }

        SplitType.AMOUNT -> if (spend.spentBy == userId) currentUserSplitValue - spend.totalAmount else currentUserSplitValue

        SplitType.SHARE -> {
            val totalShares = splits.sumOf { v -> v.value_ }
            val costPerShare = spend.totalAmount / totalShares
            if (spend.spentBy == userId)
                costPerShare * currentUserSplitValue - spend.totalAmount
            else
                costPerShare * currentUserSplitValue
        }

        SplitType.RATIO ->
            if (spend.spentBy == userId)
                spend.totalAmount * (currentUserSplitValue - 1.0)
            else
                (spend.totalAmount * currentUserSplitValue)

        SplitType.DIFFERENCE -> {
            splits.find { it.userId == userId }?.let {
                val difference = splits.sumOf { it.value_ }
                val sharePerHeadWithoutDifference =
                    (spend.totalAmount - difference) / splits.size
                if (spend.spentBy == userId)
                    sharePerHeadWithoutDifference + currentUserSplitValue - spend.totalAmount
                else
                    sharePerHeadWithoutDifference + currentUserSplitValue
            } ?: -spend.totalAmount
        }

        else -> 0.0
    }
}

suspend fun checkGroupSettles(groupId: String) = flow {
    RepositoryImpl().getSpendsAfterLastSettle(groupId).collect {
        if (it.isEmpty())
            emit(true)
        else {
            val balanceList = hashMapOf<String, Double>()
            RepositoryImpl().getGroupMemberDetails(groupId).collect {
                balanceList.putAll(
                    it.map { user ->
                        Pair(
                            user.userId,
                            0.0,
                        )
                    }
                )
            }
            balanceList.keys.forEach { k ->
                it.forEach { sp ->
                    balanceList[k] = (balanceList[k] ?: 0.0) + sp.calculateOwes(k)
                }
            }
            if (balanceList.values.all { ele -> ele == 0.0 })
                RepositoryImpl().saveGroupSettle(groupId).collect {
//                    RepositoryImpl().upSync().collect{} TODO
                    if (it)
                        emit(true)
                }
            else emit(false)
        }
    }
}