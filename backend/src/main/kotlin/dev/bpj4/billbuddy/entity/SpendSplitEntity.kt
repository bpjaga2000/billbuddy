package dev.bpj4.billbuddy.entity


import jakarta.persistence.*

@Entity
@Table(name = "spend_splits")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "lent_or_borrowed", discriminatorType = DiscriminatorType.INTEGER)
open class SpendSplitEntity(
        @Column(name = "user_id")
        open val userId: String = diglol.id.Id.generate().encodeToString(),

        @Column(name = "spend_id")
        open val spendId: String = diglol.id.Id.generate().encodeToString(),

        @Column(name = "lent_or_borrowed", insertable = false, updatable = false)
        open val lentOrBorrowed: Int = LentOrBorrowed.LENT,

        @Column(name = "split_type")
        open val splitType: Int = SplitType.EQUAL,

        @Column(name = "value")
        open val value: Float = 0f,

        @Column(name = "created_by")
        open var createdBy: String = "",

        @Column(name = "updated_by")
        open var updatedBy: String = "",

        @Column(name = "deleted_by")
        open var deletedBy: String? = null
) : BaseEntity<String>(diglol.id.Id.generate().encodeToString())