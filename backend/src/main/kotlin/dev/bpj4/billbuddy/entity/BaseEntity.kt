package dev.bpj4.billbuddy.entity

import jakarta.persistence.*

@MappedSuperclass
abstract class BaseEntity<T>(
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        open val id: T,

        @Column(name = "created_at_frontend")
        open val createdAtFrontend: Long = 0,

        @Column(name = "updated_at_frontend")
        open val updatedAtFrontend: Long = 0,

        @Column(name = "deleted_at_frontend")
        open val deletedAtFrontend: Long? = null,

        @Column(name = "created_at")
        open val createdAt: Long = 0,

        @Column(name = "updated_at")
        open val updatedAt: Long = 0,

        @Column(name = "deleted_at")
        open val deletedAt: Long? = null
)