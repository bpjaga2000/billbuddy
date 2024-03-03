package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "spend_splits")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "lent_or_borrowed", discriminatorType = DiscriminatorType.INTEGER)
abstract class SpendSplitEntity(
        @Column(name = "user_id")
        open val userId: UUID = UUID.randomUUID(),

        @Column(name = "spend_id")
        open val spendId: UUID = UUID.randomUUID(),

        @Column(name = "lent_or_borrowed", insertable = false, updatable = false)
        open val lentOrBorrowed: Int = LentOrBorrowed.LENT,

        @Column(name = "split_type")
        open val splitType: Int = SplitType.EQUAL,

        @Column(name = "value")
        open val value: Float = 0f
) : BaseEntity<String>(Xid.string())