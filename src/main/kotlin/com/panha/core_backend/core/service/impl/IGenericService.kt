package com.panha.core_backend.core.service.impl

import com.panha.core_backend.core.BaseEntity
import com.panha.core_backend.core.exception.NotFoundExceptionCustom
import com.panha.core_backend.core.repo.BaseRepository
import com.panha.core_backend.core.service.GenericService
import com.panha.core_backend.utilities.UtilService
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.criteria.Predicate
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

abstract class IGenericService<T : BaseEntity>: GenericService<T> {
    @Autowired lateinit var repository: BaseRepository<T>
    @Autowired lateinit var utilService: UtilService
    private val entityClass : Class<T>?=null
    override fun save(entity: T): T {
        return repository.save(entity)
    }

    override fun list(allParams: Map<String, String>): Page<T> {
        val page = allParams["page"]?.toInt() ?:0
        val size = allParams["size"]?.toInt() ?:10

        return repository.findAll(
            {root , _ , cb ->
                val predicates = ArrayList<Predicate>()
                predicates.add(cb.isTrue(root.get("status")))
                cb.and(*predicates.toTypedArray())
            },
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC , "id"))
        )
    }

    override fun detail(id: Long): T {
        return this.findById(id)
    }

    override fun all() : List<T> {
        return repository.findAll()
    }

    override fun update(id: Long , entity: T): T {
        val existingEntity = this.findById(id)
        try {
            utilService.bindProperties(entity, existingEntity, exclude = listOf("id", "created", "createdBy"))
            return repository.save(existingEntity)
        }catch (ex : ConstraintViolationException) {
            throw EntityNotFoundException("Unexpected Error")
        }
    }

    override fun delete(id: Long): T {
        val existingEntity = this.findById(id)
        repository.delete(existingEntity)
        return existingEntity
    }

    override fun softDelete(id: Long , status : Boolean): T {
        val existingEntity = this.findById(id)
        existingEntity.status = false
        return existingEntity
    }

    private fun findById(id: Long): T {
        return repository.findById(id)
            .orElseThrow { NotFoundExceptionCustom("${entityClass?.simpleName} with id $id not found") }
    }

}