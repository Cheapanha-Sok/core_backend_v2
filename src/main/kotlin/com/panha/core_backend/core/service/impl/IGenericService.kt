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
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class IGenericService<T : BaseEntity>: GenericService<T> {
    @Autowired lateinit var repository: BaseRepository<T>
    @Autowired lateinit var utilService: UtilService
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
            .orElseThrow { NotFoundExceptionCustom("${this.getGenericTypeClass().simpleName} with id $id not found") }
    }

    private fun getGenericTypeClass(): Class<*> {
        try {
            val genericSuperclass: Type = this.javaClass.genericSuperclass
            check(genericSuperclass is ParameterizedType) { "Class is not parametrized with generic type! Please use extends <>" }
            val className: String = (genericSuperclass as ParameterizedType).actualTypeArguments[0].typeName
            return Class.forName(className)
        } catch (e: Exception) {
            // Log detailed error
            e.printStackTrace()
            throw IllegalStateException("Unable to determine generic type class! Error: ${e.message}", e)
        }
    }

}