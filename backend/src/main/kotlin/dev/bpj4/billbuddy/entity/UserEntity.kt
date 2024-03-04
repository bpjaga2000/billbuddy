package dev.bpj4.billbuddy.entity

import com.github.shamil.Xid
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(

        val name: String = "",

        val password: String = "",

        val email: String = "",

        @Column(name = "mobile_country_code")
        val mobileCountryCode: String? = null,

        val phone: Long? = null,

        val role: String = UserRoles.FREE,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "group_members", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "group_id", referencedColumnName = "id")])
        val groups: MutableList<GroupEntity> = mutableListOf()

) : BaseEntity<String>(Xid.string())