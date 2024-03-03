package dev.bpj4.billbuddy.entity

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue(value = LentOrBorrowed.BORROWED.toString())
class BorrowedSpendEntity : SpendSplitEntity()