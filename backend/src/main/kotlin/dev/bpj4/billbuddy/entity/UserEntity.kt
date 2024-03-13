package dev.bpj4.billbuddy.entity

import diglol.id.Id
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

        @Column(name = "default_group_id")
        var defaultGroupId: String? = null

) : BaseEntity<String>(Id.generate().encodeToString())