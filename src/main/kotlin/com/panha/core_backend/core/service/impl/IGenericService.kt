package com.panha.core_backend.core.service.impl

import com.panha.core_backend.core.BaseEntity
import com.panha.core_backend.core.exception.NotFoundExceptionCustom
import com.panha.core_backend.core.repo.ExtendRepository
import com.panha.core_backend.core.service.GenericService
import com.panha.core_backend.utilities.UtilService
import org.springframework.stereotype.Service

@Service
class IGenericService<T : BaseEntity>(
    private val repository : ExtendRepository<T>,
    private val utilService: UtilService
) : GenericService<T> {

    override fun save(entity: T): T {
        return repository.save(entity)
    }

    override fun update(entity: T, id: Long): T {
        val existingEntity = this.findById(id)
        val updatedEntity = utilService.bindProperties(entity , existingEntity , exclude = listOf("id" , "created" , "createdBy"))
        repository.save(updatedEntity as T)
        return existingEntity
    }

    override fun delete(id: Long): T {
        val existingEntity = this.findById(id)
        repository.delete(existingEntity)
        return existingEntity
    }

    override fun softDelete(id: Long): T {
        val existingEntity = this.findById(id)
        existingEntity.status = false
        return existingEntity
    }

    private fun findById(id: Long): T {
        return repository.findById(id).orElseThrow { NotFoundExceptionCustom("${this.entityClass.name} with id $id not found") }
    }

    // This helper allows retrieving the class name of T
    private val entityClass: Class<T>
        get() = repository.javaClass.genericSuperclass as Class<T>
}