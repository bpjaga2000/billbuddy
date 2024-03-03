package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "group_members")
data class GroupMembersEntity(

        @Column(name = "user_id")
        val userId: UUID = UUID.randomUUID(),

        @Column(name = "group_id")
        val groupId: UUID = UUID.randomUUID(),

) : BaseEntity<String>(Xid.string())