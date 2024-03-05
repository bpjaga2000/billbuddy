package dev.bpj4.billbuddy.entity

import jakarta.persistence.*

@MappedSuperclass
abstract class BaseEntity<T>(
        @Id
        open val id: T,

        @Column(name = "created_at_frontend")
        open val createdAtFrontend: Long = System.currentTimeMillis() / 1000,

        @Column(name = "updated_at_frontend")
        open val updatedAtFrontend: Long = System.currentTimeMillis() / 1000,

        @Column(name = "deleted_at_frontend")
        open val deletedAtFrontend: Long? = null,

        @Column(name = "created_at")
        open val createdAt: Long = System.currentTimeMillis() / 1000,

        @Column(name = "updated_at")
        open val updatedAt: Long = System.currentTimeMillis() / 1000,

        @Column(name = "deleted_at")
        open val deletedAt: Long? = null
)