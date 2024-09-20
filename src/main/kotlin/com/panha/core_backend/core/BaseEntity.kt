package com.panha.core_backend.core

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(

    @JsonIgnore
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    open var createdBy: Long? = 0L,

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    open var createdAt: LocalDateTime? = LocalDateTime.now(),

    @JsonIgnore
    @LastModifiedBy
    @Column(name = "last_modified_by")
    open var updatedby: Long? = 0L,

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "last_modified_at")
    open var updatedAt: LocalDateTime? = LocalDateTime.now(),

    open var status: Boolean? = true
)