package com.panha.core_backend.core.service

import com.panha.core_backend.core.BaseEntity
import org.springframework.data.domain.Page

interface GenericService<T : BaseEntity> {
    fun list(allParams : Map<String , String>): Page<T>
    fun detail(id: Long): T
    fun all() : List<T>
    fun save(entity : T) : T
    fun update(id : Long,entity : T) : T
    fun delete(id : Long) : T
    fun softDelete(id : Long ,  status : Boolean) : T
}