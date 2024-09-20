package com.panha.core_backend.core.repo

import com.panha.core_backend.core.BaseEntity
import org.springframework.stereotype.Repository

@Repository
interface ExtendRepository<T : BaseEntity> : BaseRepository<T> {
}