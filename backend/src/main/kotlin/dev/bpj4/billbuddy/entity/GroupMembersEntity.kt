package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "group_members")
data class GroupMembersEntity(

        @Column(name = "user_id")
        val userId: String = Xid.string(),

        @Column(name = "group_id")
        val groupId: String = Xid.string()
) : BaseEntity<String>(Xid.string())