package com.panha.core_backend.core.controllers

import com.panha.core_backend.core.BaseEntity
import com.panha.core_backend.core.exception.NotFoundExceptionCustom
import com.panha.core_backend.core.repo.BaseRepository
import com.panha.core_backend.response.JsonFormat
import com.panha.core_backend.response.ResponseDTO
import com.panha.core_backend.utilities.UtilService
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.criteria.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class GenericController<T : BaseEntity>(
    private var resourceName : String? = null,
    private val isAllowedCreate : Boolean? = true,
    private val isAllowedDelete : Boolean? = true,
    private val isAllowedUpdate : Boolean? = true
) {
    @Autowired lateinit var repository: BaseRepository<T>
    @Autowired lateinit var jsonFormat: JsonFormat
    @Autowired lateinit var utilService: UtilService

    private fun getResourceName(): String {
        return resourceName?.takeIf { it.isNotBlank() } ?: run {
            val className = this.getGenericTypeClass()
            className.simpleName ?: "UnknownResource"
        }
    }

    @GetMapping("/list")
    open fun list(@RequestParam allParams: Map<String, String>): ResponseDTO {
        return jsonFormat.responsePage(
            this.listCriteria(allParams), HttpStatus.OK, "fetch ${this.getResourceName()} page"
        )
    }

    @GetMapping("/{id}")
    open fun detail(@PathVariable id: Long): ResponseDTO {
        return jsonFormat.respondObj(
            this.findById(id), HttpStatus.OK, "fetch ${this.getResourceName()} detail"
        )
    }

    @PostMapping
    open fun create(@RequestBody entity: T): ResponseDTO {
        this.checkAllowedToPerformOperation(isAllowedCreate ?: true)
        return jsonFormat.respondID(
            repository.save(entity), HttpStatus.OK, "${this.getResourceName()} created"
        )
    }

    @PutMapping("/{id}")
    open fun update(@PathVariable id: Long, @RequestBody entity: T): ResponseDTO {
        this.checkAllowedToPerformOperation(isAllowedUpdate ?: true)
        val existingEntity = this.findById(id)
        utilService.bindProperties(entity, existingEntity, exclude = listOf("id", "created", "createdBy"))
        return jsonFormat.respondObj(
            repository.save(existingEntity), HttpStatus.OK, "${this.getResourceName()} updated"
        )
    }

    @DeleteMapping("/{id}")
    open fun delete(@PathVariable id: Long): ResponseDTO {
        this.checkAllowedToPerformOperation(isAllowedDelete ?: true)
        this.findById(id) // Check if the entity exists
        repository.deleteById(id)
        return jsonFormat.respondID(
            id, HttpStatus.OK, "${this.getResourceName()} deleted"
        )
    }

    @PutMapping("/{id}/{status}")
    open fun softDelete(@PathVariable id: Long, @PathVariable status: Boolean): ResponseDTO {
        val entity = this.findById(id)
        entity.status = status
        return jsonFormat.respondObj(entity, HttpStatus.OK, "${this.getResourceName()} deleted")
    }

    private fun checkAllowedToPerformOperation(isAllowed: Boolean) {
        if (!isAllowed) throw EntityNotFoundException("This endpoint is not allowed to perform this operation")
    }

    private fun findById(id: Long): T {
        return repository.findById(id).orElseThrow {
            NotFoundExceptionCustom("${this.getResourceName()} with id $id not found")
        }
    }

    private fun listCriteria(allParams: Map<String, String>): Page<T> {
        val page = allParams["page"]?.toInt() ?: 0
        val size = allParams["size"]?.toInt() ?: 10

        return repository.findAll(
            { root, _, cb ->
                val predicates = ArrayList<Predicate>()
                predicates.add(cb.isTrue(root.get("status")))
                cb.and(*predicates.toTypedArray())
            },
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        )
    }
    fun getGenericTypeClass(): Class<*> {
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
