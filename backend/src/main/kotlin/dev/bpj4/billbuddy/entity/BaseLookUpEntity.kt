package dev.bpj4.billbuddy.entity

import jakarta.persistence.*

@MappedSuperclass
abstract class BaseLookUpEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Int = 0,

        @Column(name = "created_at")
        open val createdAt: Long = 0,

        @Column(name = "updated_at")
        open val updatedAt: Long = 0,

        @Column(name = "deleted_at")
        open val deletedAt: Long? = null
)