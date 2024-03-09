package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "group_members")
data class GroupMembersEntity(

        @Column(name = "user_id")
        val userId: String = "",

        @Column(name = "group_id")
        val groupId: String = "",

        @Column(name = "created_by")
        val createdBy: String = "",

        @Column(name = "updated_by")
        var updatedBy: String = "",

        @Column(name = "deleted_by")
        var deletedBy: String? = null

) : BaseEntity<String>(Xid.string())