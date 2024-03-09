package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "groups")
data class GroupEntity(
        val name: String = "",

        val tag: GroupTags = GroupTags.OTHER,

        @Column(name = "group_owner_id")
        val groupOwnerId: String = "",

        @Column(name = "created_by")
        val createdBy: String = "",

        @Column(name = "updated_by")
        val updatedBy: String = "",

        @Column(name = "deleted_by")
        val deletedBy: String? = null
) : BaseEntity<String>(Xid.string())