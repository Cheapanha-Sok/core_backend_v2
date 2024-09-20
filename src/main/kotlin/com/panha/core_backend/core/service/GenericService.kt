package com.panha.core_backend.core.service

import com.panha.core_backend.core.BaseEntity

interface GenericService<T : BaseEntity> {
    fun save(entity : T) : T
    fun update(entity : T , id : Long) : T
    fun delete(id : Long) : T
    fun softDelete(id : Long) : T
}