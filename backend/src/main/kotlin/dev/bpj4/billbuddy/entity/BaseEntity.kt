package dev.bpj4.billbuddy.entity

import jakarta.persistence.*

@MappedSuperclass
abstract class BaseEntity<T>(
        @Id
        open var id: T,

        @Column(name = "created_at_frontend")
        open var createdAtFrontend: Long = System.currentTimeMillis() / 1000,

        @Column(name = "updated_at_frontend")
        open var updatedAtFrontend: Long = System.currentTimeMillis() / 1000,

        @Column(name = "deleted_at_frontend")
        open var deletedAtFrontend: Long? = null,

        @Column(name = "created_at")
        open var createdAt: Long = System.currentTimeMillis() / 1000,

        @Column(name = "updated_at")
        open var updatedAt: Long = System.currentTimeMillis() / 1000,

        @Column(name = "deleted_at")
        open var deletedAt: Long? = null
)