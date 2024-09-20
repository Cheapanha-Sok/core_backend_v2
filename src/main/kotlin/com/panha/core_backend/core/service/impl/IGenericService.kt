package com.panha.core_backend.core.service.impl

import com.panha.core_backend.core.BaseEntity
import com.panha.core_backend.core.exception.NotFoundExceptionCustom
import com.panha.core_backend.core.repo.BaseRepository
import com.panha.core_backend.core.service.GenericService
import com.panha.core_backend.utilities.UtilService
import org.springframework.beans.factory.annotation.Autowired

abstract class IGenericService<T : BaseEntity>: GenericService<T> {
    @Autowired lateinit var repository: BaseRepository<T>
    @Autowired lateinit var utilService: UtilService
    override fun save(entity: T): T {
        return repository.save(entity)
    }

    override fun update(entity: T, id: Long): T {
        val existingEntity = this.findById(id)
        utilService.bindProperties(entity, existingEntity, exclude = listOf("id", "created", "createdBy"))
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
        return repository.findById(id)
            .orElseThrow { NotFoundExceptionCustom("${this.entityClass.name} with id $id not found") }
    }

    private val entityClass: Class<T>
        get() = repository::class.java.simpleName as Class<T>
}