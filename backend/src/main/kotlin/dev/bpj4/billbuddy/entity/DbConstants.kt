package dev.bpj4.billbuddy.entity

object LentOrBorrowed {
    const val LENT = 1
    const val BORROWED = 2
}

object UserRoles {
    const val PRO = "PRO"
    const val LIMITED = "LIMITED"
    const val FREE = "FREE"
}

object SplitType {
    const val EQUAL = 1
    const val RATIO = 2
    const val SHARE = 3
    const val AMOUNT = 4
    const val DIFFERENCE = 5
}