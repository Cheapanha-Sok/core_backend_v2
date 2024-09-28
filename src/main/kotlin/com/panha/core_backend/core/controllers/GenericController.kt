package com.panha.core_backend.core.controllers

import com.panha.core_backend.core.BaseEntity
import com.panha.core_backend.core.service.GenericService
import com.panha.core_backend.response.JsonFormat
import com.panha.core_backend.response.ResponseDTO
import org.springframework.beans.factory.annotation.Autowired
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
    @Autowired lateinit var genericService: GenericService<T>
    @Autowired lateinit var jsonFormat: JsonFormat

    private fun getResourceName(): String {
        return resourceName?.takeIf { it.isNotBlank() } ?: run {
            val className = this.getGenericTypeClass()
            className.simpleName ?: "UnknownResource"
        }
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

    @GetMapping("/list")
    fun list(@RequestParam allParams: Map<String, String>): ResponseDTO{
        return jsonFormat.responsePage(genericService.list(allParams) , HttpStatus.OK , "fetch ${this.getResourceName()} page")
    }
    @GetMapping("/{id}")
    fun detail(@PathVariable id : Long): ResponseDTO{
        return jsonFormat.respondObj(genericService.detail(id) , HttpStatus.OK , "fetch ${this.getResourceName()}detail")
    }
    @PostMapping
    fun create(@RequestBody entity: T): ResponseDTO{
        return jsonFormat.respondID(genericService.save(entity) , HttpStatus.OK , "${this.getResourceName()} created")
    }
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody entity: T): ResponseDTO{
        return jsonFormat.respondObj(genericService.update(id , entity) , HttpStatus.OK , "$${this.getResourceName()}updated")
    }
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseDTO{
        return jsonFormat.respondID(genericService.delete(id) , HttpStatus.OK , "$${this.getResourceName()} deleted")
    }
}