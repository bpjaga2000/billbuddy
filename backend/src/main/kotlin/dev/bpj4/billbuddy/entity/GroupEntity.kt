package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "groups")
data class GroupEntity(
        var name: String = "",

        var tag: GroupTags = GroupTags.OTHER,

        @Column(name = "group_owner_id")
        var groupOwnerId: String = "",

        @Column(name = "created_by")
        var createdBy: String = "",

        @Column(name = "updated_by")
        var updatedBy: String = "",

        @Column(name = "deleted_by")
        var deletedBy: String? = null
) : BaseEntity<String>(Xid.string())