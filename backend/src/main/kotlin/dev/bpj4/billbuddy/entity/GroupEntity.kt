package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "groups")
data class GroupEntity(
        val name: String = "",
        val tag: GroupTags = GroupTags.OTHER
) : BaseEntity<String>(Xid.string())