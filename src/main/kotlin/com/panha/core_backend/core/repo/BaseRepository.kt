package com.panha.core_backend.core.repo

import com.panha.core_backend.core.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long> , JpaSpecificationExecutor<T> {
    fun findAllByStatusIsTrue(): List<T>
}