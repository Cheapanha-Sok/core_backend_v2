package com.panha.core_backend.core.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository<T> : JpaRepository<T, Long> , JpaSpecificationExecutor<T> {
    fun findAllByStatusIsTrue(): List<T>
}