package com.panha.core_backend.core.controllers

import com.panha.core_backend.core.BaseEntity
import com.panha.core_backend.core.service.GenericService
import com.panha.core_backend.response.JsonFormat
import com.panha.core_backend.response.ResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

abstract class GenericController<T : BaseEntity> {
    @Autowired lateinit var genericService: GenericService<T>
    @Autowired lateinit var jsonFormat: JsonFormat
    private val entityClass : Class<T>?=null

    @GetMapping("/list")
    fun list(@RequestParam allParams: Map<String, String>): ResponseDTO{
        return jsonFormat.responsePage(genericService.list(allParams) , HttpStatus.OK , "fetch ${entityClass?.simpleName} page")
    }
    @GetMapping("/{id}")
    fun detail(@PathVariable id : Long): ResponseDTO{
        return jsonFormat.respondObj(genericService.detail(id) , HttpStatus.OK , "fetch ${entityClass?.simpleName} detail")
    }
    @PostMapping
    fun create(@RequestBody entity: T): ResponseDTO{
        return jsonFormat.respondID(genericService.save(entity) , HttpStatus.OK , "${entityClass?.simpleName} created")
    }
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody entity: T): ResponseDTO{
        return jsonFormat.respondObj(genericService.update(id , entity) , HttpStatus.OK , "${entityClass?.simpleName} updated")
    }
    @PostMapping("/delete/{id}/{status}")
    fun softDelete(@PathVariable id: Long, @PathVariable status: Boolean): ResponseDTO{
        return jsonFormat.respondObj(genericService.softDelete(id , status) , HttpStatus.OK , "${entityClass?.simpleName} deleted")
    }
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseDTO{
        return jsonFormat.respondID(genericService.delete(id) , HttpStatus.OK , "${entityClass?.simpleName} deleted")
    }
}