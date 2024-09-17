package core

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
data class BaseEntity(

    @JsonIgnore
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Int? = 0,

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),


    @JsonIgnore
    @LastModifiedBy
    @Column(name = "last_modified_by")
    var updatedby: Int? = 0,


    @JsonIgnore
    @LastModifiedDate
    @Column(name = "last_modified_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    var status: Boolean? = true

)