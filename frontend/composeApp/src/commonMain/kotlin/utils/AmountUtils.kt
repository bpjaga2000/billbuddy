package utils

import constants.SplitType
import data.Repository
import data.model.SpendWithSplit
import data.remote.ApiResult
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

fun checkGroupSettlesAndSync(repository: Repository, groupId: String) = flow {
    repository.getSpendsAfterLastSettle(groupId).collect {
        if (it.isEmpty()) {
            upSync(repository)
            emit(true)
        } else {
            val balanceList = hashMapOf<String, Double>()
            repository.getGroupMemberDetails(groupId).collect {
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
                repository.saveGroupSettle(groupId).collect {
                    upSync(repository)
                    if (it) {
                        emit(true)
                    }
                }
            else {
                upSync(repository)
                emit(false)
            }
        }
    }
}

suspend fun upSync(repository: Repository) {
    repository.upSync().collect {
        if (it is ApiResult.Success)
            repository.sync().collect { }
    }
}