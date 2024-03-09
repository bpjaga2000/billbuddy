package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.*

@Entity
@Table(name = "spend_splits")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "lent_or_borrowed", discriminatorType = DiscriminatorType.INTEGER)
abstract class SpendSplitEntity(
        @Column(name = "user_id")
        open val userId: String = Xid.string(),

        @Column(name = "spend_id")
        open val spendId: String = Xid.string(),

        @Column(name = "lent_or_borrowed", insertable = false, updatable = false)
        open val lentOrBorrowed: Int = LentOrBorrowed.LENT,

        @Column(name = "split_type")
        open val splitType: Int = SplitType.EQUAL,

        @Column(name = "value")
        open val value: Float = 0f,

        @Column(name = "created_by")
        open val createdBy: String = "",

        @Column(name = "updated_by")
        open val updatedBy: String = "",

        @Column(name = "deleted_by")
        open val deletedBy: String? = null
) : BaseEntity<String>(Xid.string())